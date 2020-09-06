package com.edu.mtdr.prpayment.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Multiple data source configuration
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.edu.mtdr.prpayment",
        entityManagerFactoryRef = "multiEntityManager",
        transactionManagerRef = "multiTransactionManager"
)
public class PersistenceConfiguration {
    private final String PACKAGE_SCAN = "com.edu.mtdr.prpayment";

    @Autowired
    Environment environment;

    @Primary
    @Bean(name = "shard1DS")
    @ConfigurationProperties("app.datasource.ds1")
    public DataSource shard1DS() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "shard2DS")
    @ConfigurationProperties("app.datasource.ds2")
    public DataSource shard2DS() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "shard3DS")
    @ConfigurationProperties("app.datasource.ds3")
    public DataSource shard3DS() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "multiRoutingDataSource")
    public DataSource multiRoutingDataSource(@Qualifier("shard1DS") DataSource shard1DS,
                                             @Qualifier("shard2DS") DataSource shard2DS,
                                             @Qualifier("shard3DS") DataSource shard3DS) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DbTypeEnum.SHARD1, shard1DS);
        targetDataSources.put(DbTypeEnum.SHARD2, shard2DS);
        targetDataSources.put(DbTypeEnum.SHARD3, shard3DS);
        MultiRoutingDataSource multiRoutingDataSource = new MultiRoutingDataSource();
        multiRoutingDataSource.setDefaultTargetDataSource(shard1DS());
        multiRoutingDataSource.setTargetDataSources(targetDataSources);
        return multiRoutingDataSource;
    }

    @Bean(name = "multiEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager(@Qualifier("shard1DS") DataSource shard1DS,
                                                                     @Qualifier("shard2DS") DataSource shard2DS,
                                                                     @Qualifier("shard3DS") DataSource shard3DS) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(multiRoutingDataSource(shard1DS, shard2DS, shard3DS));
        em.setPackagesToScan(PACKAGE_SCAN);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name = "multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager(@Qualifier("shard1DS") DataSource shard1DS,
                                                              @Qualifier("shard2DS") DataSource shard2DS,
                                                              @Qualifier("shard3DS") DataSource shard3DS) {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                multiEntityManager(shard1DS, shard2DS, shard3DS).getObject());
        return transactionManager;
    }

    @Primary
    @Bean(name = "dbSessionFactory")
    public LocalSessionFactoryBean dbSessionFactory(@Qualifier("shard1DS") DataSource shard1DS,
                                                    @Qualifier("shard2DS") DataSource shard2DS,
                                                    @Qualifier("shard3DS") DataSource shard3DS) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(multiRoutingDataSource(shard1DS, shard2DS, shard3DS));
        sessionFactoryBean.setPackagesToScan(PACKAGE_SCAN);
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        return sessionFactoryBean;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", true);
        properties.put(AvailableSettings.HBM2DDL_AUTO, environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        return properties;
    }
}

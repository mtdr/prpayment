package com.edu.mtdr.prpayment.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@PropertySource("classpath:persistence-multiple-db.properties")
@Configuration
@EnableTransactionManagement
public class DS1 {

    @Autowired
    Environment environment;


    @Bean(name = "entityManager")
    @ConfigurationProperties(prefix = "ds1")
    @Primary
    public DataSource DS1DataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getProperty("ds1.driverClassName"));
        dataSource.setJdbcUrl(environment.getProperty("ds1.jdbc-url"));
        dataSource.setUsername(environment.getProperty("ds1.username"));
        dataSource.setPassword(environment.getProperty("ds1.password"));
        dataSource.setCatalog("*****");
        /**
         * HikariCP specific properties. Remove if you move to other connection pooling library.
         **/
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 25000);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 20048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.addDataSourceProperty("initializationFailFast", true);
        dataSource.setPoolName("DS1_HIKARICP_CONNECTION_POOL");

        return dataSource;
    }


    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean DS1EntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(DS1DataSource());
        em.setPersistenceUnitName("DS1");
        em.setPackagesToScan("com.**.**.schema");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.HBM2DDL_AUTO, environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        em.setJpaPropertyMap(properties);
        return em;
    }


    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager DS1TransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                DS1EntityManagerFactory().getObject());
        return transactionManager;
    }

}


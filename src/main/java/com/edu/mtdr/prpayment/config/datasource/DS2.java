//package com.edu.mtdr.prpayment.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.hibernate.cfg.AvailableSettings;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//@PropertySource("classpath:persistence-multiple-db.properties")
//@Configuration
//@EnableTransactionManagement
//public class DS2 {
//
//    @Autowired
//    Environment environment;
//
//
//    @Bean(name = "DS2DataSource")
//    @ConfigurationProperties(prefix = "ds2")
//    public DataSource DS2DataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName(environment.getProperty("ds2.driverClassName"));
//        dataSource.setJdbcUrl(environment.getProperty("ds2.jdbc-url"));
//        dataSource.setUsername(environment.getProperty("ds2.username"));
//        dataSource.setPassword(environment.getProperty("ds2.password"));
//        dataSource.setCatalog("*****");
//        /**
//         * HikariCP specific properties. Remove if you move to other connection pooling library.
//         **/
//        dataSource.addDataSourceProperty("cachePrepStmts", true);
//        dataSource.addDataSourceProperty("prepStmtCacheSize", 25000);
//        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 20048);
//        dataSource.addDataSourceProperty("useServerPrepStmts", true);
//        dataSource.addDataSourceProperty("initializationFailFast", true);
//        dataSource.setPoolName("DS2_HIKARICP_CONNECTION_POOL");
//
//        return dataSource;
//    }
//
//
//    @Bean(name = "DS2EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean DS2EntityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(DS2DataSource());
//        em.setPersistenceUnitName("DS2");
//        em.setPackagesToScan("com.**.**.schema");
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put(AvailableSettings.HBM2DDL_AUTO, environment.getProperty("spring.jpa.hibernate.ddl-auto"));
//        em.setJpaPropertyMap(properties);
//        return em;
//    }
//
//
//    @Bean(name = "DS2TransactionManager")
//    public PlatformTransactionManager DS2TransactionManager() {
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                DS2EntityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//}
//

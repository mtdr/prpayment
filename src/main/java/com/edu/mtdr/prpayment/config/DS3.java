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
//public class DS3 {
//
//    @Autowired
//    Environment environment;
//
//
//    @Bean(name = "DS3DataSource")
//    @ConfigurationProperties(prefix = "ds3")
//    public DataSource DS3DataSource() {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName(environment.getProperty("ds3.driverClassName"));
//        dataSource.setJdbcUrl(environment.getProperty("ds3.jdbc-url"));
//        dataSource.setUsername(environment.getProperty("ds3.username"));
//        dataSource.setPassword(environment.getProperty("ds3.password"));
//        dataSource.setCatalog("*****");
//        /**
//         * HikariCP specific properties. Remove if you move to other connection pooling library.
//         **/
//        dataSource.addDataSourceProperty("cachePrepStmts", true);
//        dataSource.addDataSourceProperty("prepStmtCacheSize", 35000);
//        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 30048);
//        dataSource.addDataSourceProperty("useServerPrepStmts", true);
//        dataSource.addDataSourceProperty("initializationFailFast", true);
//        dataSource.setPoolName("DS3_HIKARICP_CONNECTION_POOL");
//
//        return dataSource;
//    }
//
//
//    @Bean(name = "DS3EntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean DS3EntityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(DS3DataSource());
//        em.setPersistenceUnitName("DS3");
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
//    @Bean(name = "DS3TransactionManager")
//    public PlatformTransactionManager DS3TransactionManager() {
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                DS3EntityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//}
//

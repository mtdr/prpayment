//package com.edu.mtdr.prpayment.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@EnableTransactionManagement
//@Configuration
//@PropertySource({"classpath:persistence-multiple-db.properties"})
//public class DataSourceConfig {
//    @Bean(name = "datasource1")
//    @ConfigurationProperties("ds1")
//    @Primary
//    public DataSource dataSource() {
//        DataSource dataSource = DataSourceBuilder.create().build();
//        return dataSource;
//    }
//
//    @Bean(name = "datasource2")
//    @ConfigurationProperties("ds2")
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "transactionManager")
//    @Autowired
//    @Primary
//    public PlatformTransactionManager tm1(@Qualifier("datasource1") DataSource datasource) {
//        DataSourceTransactionManager txm = new DataSourceTransactionManager(datasource);
//        return txm;
//    }
//
//    @Bean(name = "tm2")
//    @Autowired
//    public PlatformTransactionManager tm2(@Qualifier("datasource2") DataSource datasource) {
//        DataSourceTransactionManager txm = new DataSourceTransactionManager(datasource);
//        return txm;
//    }
//}

package com.edu.mtdr.prpayment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource({ "classpath:persistence-multiple-db.properties" })
public class DataSourceConfig {
    @Bean(name = "datasource1")
    @ConfigurationProperties("first-datasource")
    @Primary
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "datasource2")
    @ConfigurationProperties("second-datasource")
    public DataSource dataSource2(){
        return DataSourceBuilder.create().build();
    }
}

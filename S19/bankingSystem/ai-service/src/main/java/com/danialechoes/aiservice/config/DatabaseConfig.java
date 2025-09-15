package com.danialechoes.aiservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean(name = "customerDataSource")
    @ConfigurationProperties(prefix = "customer.datasource")
    public DataSource customerDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "depositDataSource")
    @ConfigurationProperties(prefix = "deposit.datasource")
    public DataSource depositDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "customerJdbcTemplate")
    public JdbcTemplate customerJdbcTemplate(@Qualifier("customerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "depositJdbcTemplate")
    public JdbcTemplate depositJdbcTemplate(@Qualifier("depositDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

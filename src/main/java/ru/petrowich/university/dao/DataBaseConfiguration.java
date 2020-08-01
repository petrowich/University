package ru.petrowich.university.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:db.properties")
public class DataBaseConfiguration {

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUserName;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.connectionProperties}")
    private String dbConnectionProperties;

    @Bean
    public BasicDataSource basicDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(dbDriver);
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(dbUserName);
        basicDataSource.setPassword(dbPassword);
        basicDataSource.setConnectionProperties(dbConnectionProperties);
        return basicDataSource;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(basicDataSource());
    }
}

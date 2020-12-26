package ru.petrowich.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@ComponentScan({
        "ru.petrowich.university.controller",
        "ru.petrowich.university.service",
        "ru.petrowich.university.dao",
        "ru.petrowich.university.util"
})
@EnableTransactionManagement
public class AppConfiguration {

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() throws NamingException {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() throws NamingException {
        JndiTemplate jndiTemplate = new JndiTemplate();
        return (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/UniversityDataBase");
    }
}

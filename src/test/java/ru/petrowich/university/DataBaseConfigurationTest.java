package ru.petrowich.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.FileUtils.readFileToString;

@Configuration
public class DataBaseConfigurationTest {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:initDB_h2.sql")
                .build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean(name = "populateDbSql")
    public String populateDbSql() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(requireNonNull(classLoader.getResource("populateDbTest.sql")).getFile());
        return readFileToString(file, "UTF-8");
    }
}

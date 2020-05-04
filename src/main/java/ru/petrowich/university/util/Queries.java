package ru.petrowich.university.util;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component("queries")
public class Queries {
    private Properties properties;
    private static final String SQL_PROPERTIES_PATH = "src/main/resources/sql.properties";

    private Queries() throws IOException {
        properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(SQL_PROPERTIES_PATH)) {
            properties.load(fileInputStream);
        }
    }

    public String getQuery(String key) {
        return properties.getProperty(key);
    }
}

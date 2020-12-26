package ru.petrowich.university.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

@Component("queries")
public class Queries {
    private final Properties properties;
    private static final String SQL_PROPERTIES = "sql.properties";

    private Queries() throws IOException, URISyntaxException {
        properties = new Properties();

        URL url = this.getClass().getClassLoader().getResource(SQL_PROPERTIES);
        File file = new File(url.toURI());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
        }
    }

    public String getQuery(String key) {
        return properties.getProperty(key);
    }
}

package ru.practise.pet_projects.todolistapp.utils;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

@Log4j2
public class PropertyReader {
    private final String propertiesFileName;
    private final HashMap<String, String> mapOfProperties;

    public PropertyReader(String fileName) {
        propertiesFileName = fileName;
        Properties properties = getProperties();
        mapOfProperties = new HashMap<>();
        for (Object key : properties.keySet()) {
            mapOfProperties.put(String.valueOf(key), properties.getProperty(String.valueOf(key)));
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream(propertiesFileName);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return properties;
    }

    public String get(String propertyName) {
        return mapOfProperties.get(propertyName);
    }
}

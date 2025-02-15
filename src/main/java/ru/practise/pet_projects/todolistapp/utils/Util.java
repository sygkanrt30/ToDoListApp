package ru.practise.pet_projects.todolistapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>This class loads properties from a specified properties file and provides methods
 * to access these properties. It also contains a logger for logging error messages.</p>
 */
public class Util {

    private static final String PROPERTIES_FILE_NAME = "ToDoListApp.properties";
    private final Properties properties;
    public static final Logger LOGGER = LogManager.getLogger(Util.class);

    /**
     * Constructs a {@code Util} object and loads properties from the properties file.
     */
    public Util() {
        properties = getProperties();
    }

    /**
     * <p>This method attempts to load properties from the file defined by {@code PROPERTIES_FILE_NAME}.
     * If an {@code IOException} occurs during the loading process, it logs the error and throws
     * a {@code RuntimeException}.</p>
     *
     * @return a {@code Properties} object containing the loaded properties.
     * @throws RuntimeException if an {@code IOException} occurs while loading the properties.
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return properties;
    }

    /**
     * <p>This method fetches the value associated with the given {@code propertiesName} from
     * the loaded properties.</p>
     *
     * @param propertiesName the {@code String} name of the property to retrieve.
     * @return the {@code String} value of the specified property, or {@code null} if the property does not exist.
     */
    public String getPropertiesValue(String propertiesName) {
        return properties.getProperty(propertiesName);
    }
}

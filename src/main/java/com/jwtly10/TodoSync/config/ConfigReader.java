package com.jwtly10.TodoSync.config;

import com.jwtly10.TodoSync.exceptions.MissingConfigException;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ConfigReader {

    /**
     * Read a config file and return the properties
     *
     * @param configFilePath the path to the config file
     * @return the properties
     */
    public static Optional<Properties> readConfig(String configFilePath) {
        Properties properties = new Properties();

        File file = new File(configFilePath);
        if (!file.exists()) {
            System.out.println("Config file does not exist: " + configFilePath);
            return Optional.empty();
        }

        try (FileInputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
            if (properties.isEmpty()) {
                System.out.println("Config file is empty: " + configFilePath);
                return Optional.empty();
            }
            return Optional.of(properties);
        } catch (Exception e) {
            System.out.println("Error reading config file: " + configFilePath);
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * Get the GitHub token from users config file
     * Only 2 config paths are supported for now:
     * ~/.todosync
     * ~/.config/.todosync
     * This is the order in which they are checked, if the file exists it will attempt that file.
     * If the property doesn't exist, it will not attempt the next file.
     *
     * @return the GitHub token
     */
    public static String getUserProperty(String property) {
        String baseConfigPath = System.getProperty("user.home");
        List<String> configFilePaths = List.of(AppConfig.getInstance().getProp("config.properties.files").split(","));

        for (String configFilePath : configFilePaths) {
            String fullPath = baseConfigPath + "/" + configFilePath;
            if (new java.io.File(fullPath).exists()) {
                Optional<Properties> properties = readConfig(fullPath);
                if (properties.isPresent()) {
                    if (properties.get().containsKey(property)) {
                        return properties.get().getProperty(property);
                    } else {
                        throw new MissingConfigException("Property " + property + " not found in config file: " + fullPath);
                    }
                }
            }
        }

        throw new MissingConfigException("No config file found. Please see the README for more information on configuration.");
    }


}

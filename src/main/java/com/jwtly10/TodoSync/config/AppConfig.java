package com.jwtly10.TodoSync.config;

import java.util.Properties;

public class AppConfig {
    private final Properties configProperties = new Properties();

    private AppConfig() {
        try {
            configProperties.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            System.out.println("Error loading config.properties");
            e.printStackTrace();
        }
    }

    public static AppConfig getInstance() {
        return new AppConfig();
    }

    public String getProp(String key) {
        return configProperties.getProperty(key);
    }
}


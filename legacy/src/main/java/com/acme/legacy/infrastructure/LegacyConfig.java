package com.acme.legacy.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LegacyConfig {

    private final Properties properties;

    public LegacyConfig(Properties properties) {
        this.properties = new Properties();
        this.properties.putAll(properties);
    }

    public static LegacyConfig loadFromClasspath(String resourceName) {
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new IllegalStateException("Missing configuration resource: " + resourceName);
        }

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load configuration resource: " + resourceName, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }

        return new LegacyConfig(properties);
    }

    public String getDatabaseDriver() {
        return get("db.driver");
    }

    public String getDatabaseUrl() {
        return get("db.url");
    }

    public String getDatabaseUsername() {
        return get("db.username");
    }

    public String getDatabasePassword() {
        return get("db.password");
    }

    public String getSystemCode() {
        return get("system.code");
    }

    public String getSystemOperator() {
        return get("system.operator");
    }

    public String getDefaultRegion() {
        return get("system.default.region");
    }

    public String getCurrency() {
        return get("system.currency");
    }

    public String getReportBanner() {
        return get("report.banner");
    }

    public String get(String key) {
        return properties.getProperty(key, "");
    }
}

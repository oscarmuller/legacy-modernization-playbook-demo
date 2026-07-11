package com.acme.legacy.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LegacyConnectionManager {

    private static LegacyConnectionManager instance;

    private final LegacyConfig config;

    private LegacyConnectionManager(LegacyConfig config) {
        this.config = config;
        try {
            Class.forName(config.getDatabaseDriver());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load database driver", e);
        }
    }

    public static synchronized LegacyConnectionManager getInstance(LegacyConfig config) {
        if (instance == null) {
            instance = new LegacyConnectionManager(config);
        }
        return instance;
    }

    public static synchronized void resetForTests() {
        instance = null;
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getDatabaseUrl(),
                config.getDatabaseUsername(),
                config.getDatabasePassword());
    }
}

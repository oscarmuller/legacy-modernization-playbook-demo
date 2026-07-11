package com.acme.legacy.infrastructure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class LegacyDatabaseBootstrap {

    private LegacyDatabaseBootstrap() {
    }

    public static void initialize(LegacyConnectionManager connectionManager) {
        createSchema(connectionManager);
        seedData(connectionManager);
    }

    private static void createSchema(LegacyConnectionManager connectionManager) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = connectionManager.openConnection();
            statement = connection.createStatement();
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS CUSTOMER_LEDGER (" +
                            "CUSTOMER_NUMBER VARCHAR(12) PRIMARY KEY," +
                            "COMPANY_NAME VARCHAR(80) NOT NULL," +
                            "CONTACT_NAME VARCHAR(80) NOT NULL," +
                            "REGION_CODE VARCHAR(8) NOT NULL," +
                            "CREDIT_STATUS VARCHAR(12) NOT NULL," +
                            "OUTSTANDING_BALANCE DOUBLE," +
                            "CREATED_ON DATE," +
                            "NOTES VARCHAR(255))");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not create legacy schema", e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    private static void seedData(LegacyConnectionManager connectionManager) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM CUSTOMER_LEDGER");
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return;
            }
            statement.executeUpdate(
                    "INSERT INTO CUSTOMER_LEDGER " +
                            "(CUSTOMER_NUMBER, COMPANY_NAME, CONTACT_NAME, REGION_CODE, CREDIT_STATUS, OUTSTANDING_BALANCE, CREATED_ON, NOTES) " +
                            "VALUES " +
                            "('000100','NORTHWIND INDUSTRIAL','EVELYN HART','NE','OPEN',12450.75,DATE '1994-03-18','Mainframe extract imported from branch tape')," +
                            "('000245','ATLAS FREIGHT CO','ROBERT GLASS','SE','HOLD',875.25,DATE '1992-11-02','Credit desk requested manual release before shipment')," +
                            "('000310','MERCURY OFFICE SUPPLY','ANITA COLE','MW','OPEN',45.00,DATE '1991-07-14','Legacy cash account retained for local sales office')");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not seed legacy schema", e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    private static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }
}

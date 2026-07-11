package com.acme.legacy.repository;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.infrastructure.LegacyConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

public class JdbcCustomerDao implements CustomerDao {

    private final LegacyConnectionManager connectionManager;

    public JdbcCustomerDao(LegacyConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Vector findAll() {
        Vector rows = new Vector();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.openConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    "SELECT CUSTOMER_NUMBER, COMPANY_NAME, CONTACT_NAME, REGION_CODE, CREDIT_STATUS, OUTSTANDING_BALANCE, CREATED_ON, NOTES " +
                            "FROM CUSTOMER_LEDGER ORDER BY CUSTOMER_NUMBER");

            while (resultSet.next()) {
                rows.addElement(readCustomer(resultSet));
            }

            return rows;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read customer ledger", e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    @Override
    public Customer findByNumber(String customerNumber) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.openConnection();
            statement = connection.prepareStatement(
                    "SELECT CUSTOMER_NUMBER, COMPANY_NAME, CONTACT_NAME, REGION_CODE, CREDIT_STATUS, OUTSTANDING_BALANCE, CREATED_ON, NOTES " +
                            "FROM CUSTOMER_LEDGER WHERE CUSTOMER_NUMBER = ?");
            statement.setString(1, customerNumber);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return readCustomer(resultSet);
            }

            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not locate customer " + customerNumber, e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
    }

    @Override
    public void save(Customer customer) {
        if (findByNumber(customer.getCustomerNumber()) == null) {
            insert(customer);
        } else {
            update(customer);
        }
    }

    private void insert(Customer customer) {
        executeSave(
                "INSERT INTO CUSTOMER_LEDGER (CUSTOMER_NUMBER, COMPANY_NAME, CONTACT_NAME, REGION_CODE, CREDIT_STATUS, OUTSTANDING_BALANCE, CREATED_ON, NOTES) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                customer);
    }

    private void update(Customer customer) {
        executeSave(
                "UPDATE CUSTOMER_LEDGER SET COMPANY_NAME = ?, CONTACT_NAME = ?, REGION_CODE = ?, CREDIT_STATUS = ?, " +
                        "OUTSTANDING_BALANCE = ?, CREATED_ON = ?, NOTES = ? WHERE CUSTOMER_NUMBER = ?",
                customer);
    }

    private void executeSave(String sql, Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionManager.openConnection();
            statement = connection.prepareStatement(sql);

            if (sql.startsWith("INSERT")) {
                fillInsert(statement, customer);
            } else {
                fillUpdate(statement, customer);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save customer " + customer.getCustomerNumber(), e);
        } finally {
            close(statement);
            close(connection);
        }
    }

    private void fillInsert(PreparedStatement statement, Customer customer) throws SQLException {
        statement.setString(1, customer.getCustomerNumber());
        statement.setString(2, customer.getCompanyName());
        statement.setString(3, customer.getContactName());
        statement.setString(4, customer.getRegionCode());
        statement.setString(5, customer.getCreditStatus());
        statement.setDouble(6, customer.getOutstandingBalance());
        statement.setDate(7, new java.sql.Date(customer.getCreatedOn().getTime()));
        statement.setString(8, customer.getNotes());
    }

    private void fillUpdate(PreparedStatement statement, Customer customer) throws SQLException {
        statement.setString(1, customer.getCompanyName());
        statement.setString(2, customer.getContactName());
        statement.setString(3, customer.getRegionCode());
        statement.setString(4, customer.getCreditStatus());
        statement.setDouble(5, customer.getOutstandingBalance());
        statement.setDate(6, new java.sql.Date(customer.getCreatedOn().getTime()));
        statement.setString(7, customer.getNotes());
        statement.setString(8, customer.getCustomerNumber());
    }

    private Customer readCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerNumber(resultSet.getString("CUSTOMER_NUMBER"));
        customer.setCompanyName(resultSet.getString("COMPANY_NAME"));
        customer.setContactName(resultSet.getString("CONTACT_NAME"));
        customer.setRegionCode(resultSet.getString("REGION_CODE"));
        customer.setCreditStatus(resultSet.getString("CREDIT_STATUS"));
        customer.setOutstandingBalance(resultSet.getDouble("OUTSTANDING_BALANCE"));
        customer.setCreatedOn(new Date(resultSet.getDate("CREATED_ON").getTime()));
        customer.setNotes(resultSet.getString("NOTES"));
        return customer;
    }

    private void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }
}

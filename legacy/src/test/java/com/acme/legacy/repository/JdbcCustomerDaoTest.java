package com.acme.legacy.repository;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.infrastructure.LegacyConfig;
import com.acme.legacy.infrastructure.LegacyConnectionManager;
import com.acme.legacy.infrastructure.LegacyDatabaseBootstrap;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JdbcCustomerDaoTest {

    private JdbcCustomerDao customerDao;

    @Before
    public void setUp() {
        Properties properties = new Properties();
        properties.setProperty("db.driver", "org.h2.Driver");
        properties.setProperty("db.url", "jdbc:h2:mem:legacydao" + System.nanoTime() + ";MODE=Oracle;DB_CLOSE_DELAY=-1");
        properties.setProperty("db.username", "sa");
        properties.setProperty("db.password", "");

        LegacyConnectionManager.resetForTests();
        LegacyConnectionManager connectionManager = LegacyConnectionManager.getInstance(new LegacyConfig(properties));
        LegacyDatabaseBootstrap.initialize(connectionManager);
        customerDao = new JdbcCustomerDao(connectionManager);
    }

    @Test
    public void shouldReturnSeededCustomers() {
        Vector customers = customerDao.findAll();

        assertEquals(3, customers.size());
    }

    @Test
    public void shouldInsertNewCustomer() {
        Customer customer = new Customer();
        customer.setCustomerNumber("000777");
        customer.setCompanyName("Terminal Supply");
        customer.setContactName("Margo Flynn");
        customer.setRegionCode("SW");
        customer.setCreditStatus("OPEN");
        customer.setOutstandingBalance(77.25D);
        customer.setCreatedOn(new Date());
        customer.setNotes("Loaded from branch worksheet");

        customerDao.save(customer);

        assertNotNull(customerDao.findByNumber("000777"));
        assertEquals(4, customerDao.findAll().size());
    }
}

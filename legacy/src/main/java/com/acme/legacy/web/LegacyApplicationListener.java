package com.acme.legacy.web;

import com.acme.legacy.infrastructure.LegacyConfig;
import com.acme.legacy.infrastructure.LegacyConnectionManager;
import com.acme.legacy.infrastructure.LegacyDatabaseBootstrap;
import com.acme.legacy.repository.CustomerDao;
import com.acme.legacy.repository.JdbcCustomerDao;
import com.acme.legacy.service.CustomerService;
import com.acme.legacy.service.impl.CustomerServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LegacyApplicationListener implements ServletContextListener {

    public static final String CONFIG_KEY = "legacyConfig";
    public static final String SERVICE_KEY = "customerService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        LegacyConfig config = LegacyConfig.loadFromClasspath("app.properties");
        LegacyConnectionManager connectionManager = LegacyConnectionManager.getInstance(config);
        CustomerDao customerDao = new JdbcCustomerDao(connectionManager);
        CustomerService customerService = new CustomerServiceImpl(customerDao);

        LegacyDatabaseBootstrap.initialize(connectionManager);

        servletContext.setAttribute(CONFIG_KEY, config);
        servletContext.setAttribute(SERVICE_KEY, customerService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LegacyConnectionManager.resetForTests();
    }
}

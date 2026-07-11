package com.acme.legacy.service.impl;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.repository.CustomerDao;
import com.acme.legacy.service.CustomerService;

import java.util.Date;
import java.util.Vector;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Vector listCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Customer findCustomer(String customerNumber) {
        return customerDao.findByNumber(customerNumber);
    }

    @Override
    public void registerCustomer(Customer customer) {
        if (customer.getCreatedOn() == null) {
            customer.setCreatedOn(new Date());
        }
        customer.setCustomerNumber(clean(customer.getCustomerNumber()).toUpperCase());
        customer.setCompanyName(clean(customer.getCompanyName()));
        customer.setContactName(clean(customer.getContactName()));
        customer.setRegionCode(clean(customer.getRegionCode()).toUpperCase());
        customer.setCreditStatus(clean(customer.getCreditStatus()).toUpperCase());
        customer.setNotes(clean(customer.getNotes()));
        customerDao.save(customer);
    }

    private String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }
}

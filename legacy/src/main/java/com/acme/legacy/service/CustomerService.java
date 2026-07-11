package com.acme.legacy.service;

import com.acme.legacy.domain.Customer;

import java.util.Vector;

public interface CustomerService {
    Vector listCustomers();

    Customer findCustomer(String customerNumber);

    void registerCustomer(Customer customer);
}

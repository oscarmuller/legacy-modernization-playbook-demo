package com.acme.legacy.service;

import com.acme.legacy.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> listCustomers();

    Customer getCustomer(Long id);

    void registerCustomer(Customer customer);
}

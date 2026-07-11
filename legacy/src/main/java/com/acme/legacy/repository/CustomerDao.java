package com.acme.legacy.repository;

import com.acme.legacy.domain.Customer;

import java.util.Vector;

public interface CustomerDao {
    Vector findAll();

    Customer findByNumber(String customerNumber);

    void save(Customer customer);
}

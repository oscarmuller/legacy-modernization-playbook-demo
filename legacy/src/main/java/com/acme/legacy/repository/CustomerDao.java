package com.acme.legacy.repository;

import com.acme.legacy.domain.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> findAll();

    Customer findById(Long id);

    void save(Customer customer);
}

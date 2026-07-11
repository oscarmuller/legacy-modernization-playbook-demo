package com.acme.legacy.service.impl;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.repository.CustomerDao;
import com.acme.legacy.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> listCustomers() {
        return customerDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        return customerDao.findById(id);
    }

    @Override
    public void registerCustomer(Customer customer) {
        customerDao.save(customer);
    }
}

package com.acme.legacy.web;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.service.CustomerService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomerAction extends ActionSupport {

    @Autowired
    private CustomerService customerService;

    private List<Customer> customers;

    public String execute() {
        customers = customerService.listCustomers();
        return SUCCESS;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}

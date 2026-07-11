package com.acme.legacy.web;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerSaveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerService customerService = (CustomerService) getServletContext().getAttribute(LegacyApplicationListener.SERVICE_KEY);
        Customer customer = new Customer();

        customer.setCustomerNumber(request.getParameter("customerNumber"));
        customer.setCompanyName(request.getParameter("companyName"));
        customer.setContactName(request.getParameter("contactName"));
        customer.setRegionCode(request.getParameter("regionCode"));
        customer.setCreditStatus(request.getParameter("creditStatus"));
        customer.setOutstandingBalance(parseAmount(request.getParameter("outstandingBalance")));
        customer.setNotes(request.getParameter("notes"));

        customerService.registerCustomer(customer);
        response.sendRedirect(request.getContextPath() + "/customers");
    }

    private double parseAmount(String amount) {
        if (amount == null || amount.trim().length() == 0) {
            return 0.0D;
        }

        return Double.parseDouble(amount.trim());
    }
}

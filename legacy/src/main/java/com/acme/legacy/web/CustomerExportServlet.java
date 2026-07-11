package com.acme.legacy.web;

import com.acme.legacy.infrastructure.LegacyXmlMarshaller;
import com.acme.legacy.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerExportServlet extends HttpServlet {

    private final LegacyXmlMarshaller marshaller = new LegacyXmlMarshaller();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerService customerService = (CustomerService) getServletContext().getAttribute(LegacyApplicationListener.SERVICE_KEY);

        response.setContentType("text/xml;charset=UTF-8");
        response.getWriter().write(marshaller.toXml(customerService.listCustomers()));
    }
}

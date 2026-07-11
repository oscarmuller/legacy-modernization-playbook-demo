package com.acme.legacy.web;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LegacyCustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/customers")
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("customer/list");
        mv.addObject("customers", customerService.listCustomers());
        mv.addObject("clientIp", request.getRemoteAddr());
        return mv;
    }

    @RequestMapping("/customers/save")
    public String save(@ModelAttribute Customer customer) {
        customerService.registerCustomer(customer);
        return "redirect:/customers";
    }
}

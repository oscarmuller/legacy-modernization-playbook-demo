package com.acme.legacy.web;

import com.acme.legacy.domain.Customer;
import com.acme.legacy.infrastructure.LegacyConfig;
import com.acme.legacy.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public class CustomerListServlet extends HttpServlet {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final DecimalFormat amountFormat = new DecimalFormat("0.00");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerService customerService = (CustomerService) getServletContext().getAttribute(LegacyApplicationListener.SERVICE_KEY);
        LegacyConfig config = (LegacyConfig) getServletContext().getAttribute(LegacyApplicationListener.CONFIG_KEY);
        Vector customers = customerService.listCustomers();
        PrintWriter writer = response.getWriter();
        Enumeration items = customers.elements();
        double totalBalance = 0.0D;
        String userAgent = request.getHeader("User-Agent");

        response.setContentType("text/html;charset=UTF-8");

        writer.println("<html>");
        writer.println("<head><title>Customer Ledger</title></head>");
        writer.println("<body bgcolor=\"#F2F2E6\" text=\"#000000\">");
        writer.println("<font face=\"Courier New, Courier, monospace\" size=\"2\">");
        writer.println("<h2>" + config.getReportBanner() + "</h2>");
        writer.println("<p>BRANCH OPERATOR: " + safe(config.getSystemOperator()) + "<br/>");
        writer.println("PRINT DATE: " + dateFormat.format(new Date()) + "<br/>");
        if (userAgent != null && userAgent.indexOf("MSIE") >= 0) {
            writer.println("BROWSER MODE: INTERNET EXPLORER COMPATIBLE<br/>");
        } else {
            writer.println("BROWSER MODE: GENERIC HTML TERMINAL<br/>");
        }
        writer.println("</p>");

        writer.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" width=\"100%\">");
        writer.println("<tr bgcolor=\"#CCCC99\">");
        writer.println("<th>NO.</th><th>COMPANY</th><th>CONTACT</th><th>REGION</th><th>STATUS</th><th>BALANCE</th><th>OPENED</th><th>NOTES</th>");
        writer.println("</tr>");

        while (items.hasMoreElements()) {
            Customer customer = (Customer) items.nextElement();
            totalBalance = totalBalance + customer.getOutstandingBalance();

            writer.println("<tr>");
            writer.println("<td>" + safe(customer.getCustomerNumber()) + "</td>");
            writer.println("<td>" + safe(customer.getCompanyName()) + "</td>");
            writer.println("<td>" + safe(customer.getContactName()) + "</td>");
            writer.println("<td>" + safe(customer.getRegionCode()) + "</td>");
            writer.println("<td>" + safe(customer.getCreditStatus()) + "</td>");
            writer.println("<td align=\"right\">" + amountFormat.format(customer.getOutstandingBalance()) + "</td>");
            writer.println("<td>" + dateFormat.format(customer.getCreatedOn()) + "</td>");
            writer.println("<td>" + safe(customer.getNotes()) + "</td>");
            writer.println("</tr>");
        }

        writer.println("<tr bgcolor=\"#E0E0CC\">");
        writer.println("<td colspan=\"5\" align=\"right\"><b>TOTAL OUTSTANDING</b></td>");
        writer.println("<td align=\"right\"><b>" + amountFormat.format(totalBalance) + "</b></td>");
        writer.println("<td colspan=\"2\">CURRENCY: " + safe(config.getCurrency()) + "</td>");
        writer.println("</tr>");
        writer.println("</table>");

        writer.println("<br/>");
        writer.println("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" width=\"100%\">");
        writer.println("<tr bgcolor=\"#CCCC99\"><th colspan=\"2\">ADD / UPDATE CUSTOMER ACCOUNT</th></tr>");
        writer.println("<tr><td width=\"30%\">");
        writer.println("<form method=\"post\" action=\"customer-save\">");
        writer.println("CUSTOMER NO.<br/><input type=\"text\" name=\"customerNumber\" size=\"12\" maxlength=\"12\"/><br/><br/>");
        writer.println("COMPANY<br/><input type=\"text\" name=\"companyName\" size=\"40\" maxlength=\"80\"/><br/><br/>");
        writer.println("CONTACT<br/><input type=\"text\" name=\"contactName\" size=\"40\" maxlength=\"80\"/><br/><br/>");
        writer.println("REGION<br/><input type=\"text\" name=\"regionCode\" size=\"8\" maxlength=\"8\" value=\"" + safe(config.getDefaultRegion()) + "\"/><br/><br/>");
        writer.println("STATUS<br/><input type=\"text\" name=\"creditStatus\" size=\"12\" maxlength=\"12\" value=\"OPEN\"/><br/><br/>");
        writer.println("BALANCE<br/><input type=\"text\" name=\"outstandingBalance\" size=\"12\" maxlength=\"12\" value=\"0.00\"/><br/><br/>");
        writer.println("NOTES<br/><textarea name=\"notes\" cols=\"45\" rows=\"4\"></textarea><br/><br/>");
        writer.println("<input type=\"submit\" value=\"SAVE ACCOUNT\"/>");
        writer.println("</form>");
        writer.println("</td><td valign=\"top\">");
        writer.println("<b>OPERATIONS NOTES</b><br/>");
        writer.println("1. Customer number is branch assigned and must remain zero-padded.<br/>");
        writer.println("2. Status HOLD means orders must be released by credit desk.<br/>");
        writer.println("3. XML export is consumed nightly by the warehouse batch job.<br/>");
        writer.println("4. Screen layout optimized for 800x600 CRT monitors.<br/>");
        writer.println("</td></tr>");
        writer.println("</table>");

        writer.println("</font>");
        writer.println("</body>");
        writer.println("</html>");
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

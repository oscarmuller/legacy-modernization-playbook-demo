package com.acme.legacy.infrastructure;

import com.acme.legacy.domain.Customer;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Vector;

public class LegacyXmlMarshaller {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public String toXml(Vector customers) {
        StringBuffer xml = new StringBuffer(2048);
        Enumeration items = customers.elements();

        xml.append("<?xml version=\"1.0\"?>\n");
        xml.append("<customer-ledger generated-on=\"").append(dateFormat.format(new java.util.Date())).append("\">\n");

        while (items.hasMoreElements()) {
            Customer customer = (Customer) items.nextElement();
            xml.append("  <customer number=\"").append(escape(customer.getCustomerNumber())).append("\">\n");
            xml.append("    <company-name>").append(escape(customer.getCompanyName())).append("</company-name>\n");
            xml.append("    <contact-name>").append(escape(customer.getContactName())).append("</contact-name>\n");
            xml.append("    <region-code>").append(escape(customer.getRegionCode())).append("</region-code>\n");
            xml.append("    <credit-status>").append(escape(customer.getCreditStatus())).append("</credit-status>\n");
            xml.append("    <outstanding-balance>").append(customer.getOutstandingBalance()).append("</outstanding-balance>\n");
            xml.append("    <created-on>").append(dateFormat.format(customer.getCreatedOn())).append("</created-on>\n");
            xml.append("    <notes>").append(escape(customer.getNotes())).append("</notes>\n");
            xml.append("  </customer>\n");
        }

        xml.append("</customer-ledger>\n");
        return xml.toString();
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        StringBuffer escaped = new StringBuffer(value.length());
        int index;

        for (index = 0; index < value.length(); index++) {
            char token = value.charAt(index);
            if (token == '&') {
                escaped.append("&amp;");
            } else if (token == '<') {
                escaped.append("&lt;");
            } else if (token == '>') {
                escaped.append("&gt;");
            } else if (token == '"') {
                escaped.append("&quot;");
            } else {
                escaped.append(token);
            }
        }

        return escaped.toString();
    }
}

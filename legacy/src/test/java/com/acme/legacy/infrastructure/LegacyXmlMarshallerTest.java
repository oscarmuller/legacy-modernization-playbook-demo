package com.acme.legacy.infrastructure;

import com.acme.legacy.domain.Customer;
import org.junit.Test;

import java.util.Date;
import java.util.Vector;

import static org.junit.Assert.assertTrue;

public class LegacyXmlMarshallerTest {

    @Test
    public void shouldRenderCustomerLedgerXml() {
        LegacyXmlMarshaller marshaller = new LegacyXmlMarshaller();
        Vector customers = new Vector();
        Customer customer = new Customer();

        customer.setCustomerNumber("000999");
        customer.setCompanyName("Acme & Sons");
        customer.setContactName("Dana West");
        customer.setRegionCode("NE");
        customer.setCreditStatus("OPEN");
        customer.setOutstandingBalance(12.5D);
        customer.setCreatedOn(new Date(0L));
        customer.setNotes("Night shift");
        customers.addElement(customer);

        String xml = marshaller.toXml(customers);

        assertTrue(xml.contains("<customer number=\"000999\">"));
        assertTrue(xml.contains("<company-name>Acme &amp; Sons</company-name>"));
        assertTrue(xml.contains("<notes>Night shift</notes>"));
    }
}

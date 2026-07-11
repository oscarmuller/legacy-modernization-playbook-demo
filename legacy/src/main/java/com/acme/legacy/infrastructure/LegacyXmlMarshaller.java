package com.acme.legacy.infrastructure;

import com.acme.legacy.domain.Customer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class LegacyXmlMarshaller {

    public String toXml(Customer customer) {
        try {
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(customer, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            throw new IllegalStateException("Legacy JAXB marshalling failed", ex);
        }
    }
}

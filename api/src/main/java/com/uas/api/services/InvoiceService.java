package com.uas.api.services;

import com.uas.api.models.entities.Orders;

public interface InvoiceService {
    /**
     * Generates a pdf for an order.
     * @param givenOrder The order a pdf is being generated for.
     * @return Returns the location and name of the pdf.
     */
    String generatePDF(Orders givenOrder);
    /**
     * Sends a given invoice pdf to a given address.
     * @param invoicePath The path to the invoice with the invoice name.
     * @param recipientAddress The email address of the supplier
     * @return returns true for success and false for error.
     */
    boolean emailInvoice(String invoicePath, String recipientAddress);
}

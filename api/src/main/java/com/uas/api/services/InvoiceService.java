package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.entities.Orders;

public interface InvoiceService {
    /**
     * Generated a invoiceDTO to pass to the generate pdf method.
     * @param givenOrder The order the invoice is being created for.
     * @return an invoiceDTO.
     */
    InvoiceDTO getInvoiceData(Orders givenOrder);
    /**
     * Generates a pdf for an order.
     * @param invoiceDTO of the order that a pdf is being generated for.
     * @return Returns the location and name of the pdf.
     */
    String generatePDF(InvoiceDTO invoiceDTO);
    /**
     * Sends a given invoice pdf to a given address.
     * @param invoicePath The path to the invoice with the invoice name.
     * @param recipientAddress The email address of the supplier
     * @return returns true for success and false for error.
     */
    boolean emailInvoice(String invoicePath, String recipientAddress);


}

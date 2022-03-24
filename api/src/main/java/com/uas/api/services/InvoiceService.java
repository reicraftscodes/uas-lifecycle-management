package com.uas.api.services;

import com.uas.api.models.entities.Orders;
import com.uas.api.requests.MoreStockRequest;

public interface InvoiceService {

    String generatePDF(Orders givenOrder);
}

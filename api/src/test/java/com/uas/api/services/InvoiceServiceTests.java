package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.repositories.StockToOrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTests {

    @Mock
    private StockToOrdersRepository stockToOrdersRepository;

    @Autowired
    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    public void getInvoiceDataSuccess() {
        List<StockToOrders> stockOrder = new ArrayList<>();
        Timestamp ts = Timestamp.from(Instant.now());
        Location location = new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales");
        Orders order = new Orders(1, location,"sncmsuktestemail@gmail.com",1000, ts);
        PartType wingA = new PartType(1l, PartName.WING_A);
        PartType wingB = new PartType(2l, PartName.WING_B);
        Part partA = new Part(1l, wingA,"Wing A v1",ts.toLocalDateTime(),BigDecimal.valueOf(500),1000l,1000l);
        Part partB = new Part(2l,wingB,"Wing B v1",ts.toLocalDateTime(),BigDecimal.valueOf(500),1000l,1000l);
        StockToOrders stock1 = new StockToOrders(order,partA,1);
        StockToOrders stock2 = new StockToOrders(order,partB,1);
        stockOrder.add(stock1);
        stockOrder.add(stock2);

        when(stockToOrdersRepository.findAllByOrderID(1l)).thenReturn(stockOrder);

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceData(order);

        assertEquals("OrderID should be 1",1l,invoiceDTO.getOrderID());
        assertEquals("Time should be same as time set",ts.toString(),invoiceDTO.getGenerationTime());
        assertEquals("Location Should be St Athen: ","St Athen",invoiceDTO.getDeliveryLocation().getLocationName());
        assertEquals("Supplier email same:","sncmsuktestemail@gmail.com",invoiceDTO.getSupplierEmail());
        assertEquals("Total cost is 2000:",1000.0,invoiceDTO.getTotalCost());
        assertEquals("Expected part orders:",stockOrder,invoiceDTO.getPartOrders());
    }

    @Test
    public void generatePDFSuccess() {
        Location location = new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales");
        Timestamp ts = Timestamp.from(Instant.now());
        List<StockToOrders> stockOrder = new ArrayList<>();
        Orders order = new Orders(-1, location,"sncmsuktestemail@gmail.com",1000, ts);
        PartType wingA = new PartType(1l, PartName.WING_A);
        PartType wingB = new PartType(2l, PartName.WING_B);
        Part partA = new Part(1l, wingA,"Wing A v1",ts.toLocalDateTime(),BigDecimal.valueOf(500),1000l,1000l);
        Part partB = new Part(2l,wingB,"Wing B v1",ts.toLocalDateTime(),BigDecimal.valueOf(500),1000l,1000l);
        StockToOrders stock1 = new StockToOrders(order,partA,1);
        StockToOrders stock2 = new StockToOrders(order,partB,1);
        stockOrder.add(stock1);
        stockOrder.add(stock2);

        InvoiceDTO invoiceDTO = new InvoiceDTO(-1,"sncmsuktestemail@gmail.com",order.getOrderDateTime().toString(),location,stockOrder,2100);

        String result = invoiceService.generatePDF(invoiceDTO);

        File invoicePDF = new File("invoices/order_-1.pdf");
        boolean exists = invoicePDF.exists();

        assertEquals("Filename path is returned","invoices/order_-1.pdf",result);
        assertEquals("File is created:",true,exists);

        if (exists) {
            invoicePDF.delete();
        }
    }

    @Test
    public void emailInvoiceFailure() {
        boolean result = invoiceService.emailInvoice(null,null);

        assertEquals("Should fail with no email or pdf: ",false,result);
    }
}

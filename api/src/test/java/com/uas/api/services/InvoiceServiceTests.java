package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Orders;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.StockToOrders;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.repositories.StockToOrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
        Orders order = new Orders(1, location,2100,"sncmsuktestemail@gmail.com", ts);
        PartType wingA = new PartType(1l, PartName.WING_A, BigDecimal.valueOf(300),1500l,100l);
        PartType wingB = new PartType(2l, PartName.WING_B, BigDecimal.valueOf(300),1500l,100l);
        StockToOrders stock1 = new StockToOrders(order,wingA,4);
        StockToOrders stock2 = new StockToOrders(order,wingB,3);
        stockOrder.add(stock1);
        stockOrder.add(stock2);

        when(stockToOrdersRepository.findAllByOrderID(1)).thenReturn(stockOrder);

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceData(order);

        assertEquals("OrderID should be 1",1l,invoiceDTO.getOrderID());
        assertEquals("Time should be same as time set",ts.toString(),invoiceDTO.getGenerationTime());
        assertEquals("Location Should be St Athen: ","St Athen",invoiceDTO.getDeliveryLocation().getLocationName());
        assertEquals("Supplier email same:","sncmsuktestemail@gmail.com",invoiceDTO.getSupplierEmail());
        assertEquals("Total cost is 2000:",2100.0,invoiceDTO.getTotalCost());
        assertEquals("Expected part orders:",stockOrder,invoiceDTO.getPartOrders());
    }
}

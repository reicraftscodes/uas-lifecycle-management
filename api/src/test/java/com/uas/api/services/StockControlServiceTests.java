package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Orders;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.OrdersRepository;
import com.uas.api.repositories.PartTypeRepository;
import com.uas.api.repositories.StockToOrdersRepository;
import com.uas.api.requests.MoreStockRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockControlServiceTests {
    @Mock
    private LocationRepository locationRepository;

    @Mock
    private StockToOrdersRepository stockToOrdersRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private PartTypeRepository partTypeRepository;

    @Mock
    private InvoiceServiceImpl invoiceService;

    @InjectMocks
    @MockBean
    private StockControlServiceImpl stockControlService;

    @Test
    public void orderMoreStockWithBadLocation() {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Fake Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            when(stockControlService.addMoreStock(newStock)).thenReturn(new StockControlServiceImpl.StockReceipt("1000"));
            stockControlService.addMoreStock(newStock);
        });
        Assertions.assertEquals("Location does not exist!", thrown.getMessage());
    }

    @Test
    public void orderMoreStockWithMoreQuantitiesThanTypes() {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        quantities.add(10);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        IndexOutOfBoundsException thrown = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            stockControlService.addMoreStock(newStock);
        });
        Assertions.assertEquals("Missing quantity for part type in order!", thrown.getMessage());
    }
    @Test
    public void orderMoreStockWithMoreTypesThanQuantities() {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        partTypes.add(111L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        IndexOutOfBoundsException thrown = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            stockControlService.addMoreStock(newStock);
        });
        Assertions.assertEquals("Missing quantity for part type in order!", thrown.getMessage());
    }
    @Test
    @Transactional
    public void validOrder() {
        PartType mockPartType = new PartType(1L, PartName.WING_A, BigDecimal.valueOf(100), 40L, 550L);
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        Orders order = new Orders(1, mockLocation,100,"sncmsuktestemail@gmail.com", Timestamp.from(Instant.now()));
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        when(partTypeRepository.findPartTypeById(anyLong())).thenReturn(Optional.of(mockPartType));
        when(ordersRepository.findByAttributes(any(),any())).thenReturn(order);
        when(invoiceService.getInvoiceData(any())).thenReturn(new InvoiceDTO());
        when(invoiceService.generatePDF(any())).thenReturn("");
        when(invoiceService.emailInvoice(any(),any())).thenReturn(true);

        StockControlServiceImpl.StockReceipt receipt = stockControlService.addMoreStock(newStock);
        Assertions.assertEquals("100.0", receipt.getCost());
    }

}

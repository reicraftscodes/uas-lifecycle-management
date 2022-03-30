package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.dtos.StockOrderDTO;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.repositories.*;
import com.uas.api.requests.MoreStockRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    private InvoiceServiceImpl invoiceService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private PartRepository partRepository;

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
            when(stockControlService.addMoreStock(newStock)).thenReturn(new StockControlServiceImpl.StockReceipt(String.valueOf(1000)));
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
        PartType mockPartType = new PartType(1L, PartName.WING_A);
        Part mockPart = new Part(mockPartType, "Mock Wing A", BigDecimal.valueOf(1000L), 750, 0);//, BigDecimal.valueOf(100), 40L, 550L);
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        when(partRepository.findPartBypartNumber(anyLong())).thenReturn(Optional.of(mockPart));
        when(invoiceService.getInvoiceData(any())).thenReturn(new InvoiceDTO());
        when(invoiceService.generatePDF(any())).thenReturn("");
        when(invoiceService.emailInvoice(any(),any())).thenReturn(true);
        StockControlServiceImpl.StockReceipt receipt = stockControlService.addMoreStock(newStock);
        Assertions.assertEquals("1000.0", receipt.getCost());
    }

    @Test
    public void whenGetAllPartStockOrdersThenReturnList() {
        List<StockToOrders> stockToOrders = new ArrayList<>();
        Location mockLocation = new Location();
        mockLocation.setLocationName("Cardiff");

        Part mockPart = new Part();
        mockPart.setPartType(new PartType(1L, PartName.MOTOR));
        stockToOrders.add(new StockToOrders(
                new Orders(1, mockLocation, "supplierOne@test.com", 2000.00, Timestamp.valueOf("2022-01-29 11:17:43")),
                mockPart,
                15));

        stockToOrders.add(new StockToOrders(
                new Orders(2, mockLocation, "supplierTwo@test.com", 2000.00, Timestamp.valueOf("2022-01-29 11:17:43")),
                mockPart,
                30));

        when(stockToOrdersRepository.findAll()).thenReturn(stockToOrders);
        List<StockOrderDTO> stockOrderDTOList = stockControlService.getAllPreviousStockOrders();

        Assertions.assertEquals(2, stockOrderDTOList.size());
        Assertions.assertEquals("supplierOne@test.com", stockOrderDTOList.get(0).getSupplierEmail());
        Assertions.assertEquals("supplierTwo@test.com", stockOrderDTOList.get(1).getSupplierEmail());
        Assertions.assertEquals(15, stockOrderDTOList.get(0).getQuantity());
        Assertions.assertEquals(30, stockOrderDTOList.get(1).getQuantity());
    }

}

package com.uas.api.services;

import com.uas.api.models.entities.Location;
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

import java.util.ArrayList;
import java.util.Optional;

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

    @InjectMocks
    @MockBean
    private StockControlServiceImpl stockControlService;

    @Test
    public void orderMoreStockWithBadLocation() {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Fake Cardiff", 40.00, partTypes, quantities);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            when(stockControlService.addMoreStock(newStock)).thenReturn(new StockControlServiceImpl.StockReceipt(String.valueOf(newStock.getCost())));
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
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", 40.00, partTypes, quantities);
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
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", 40.00, partTypes, quantities);
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
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", 40.00, partTypes, quantities);
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        StockControlServiceImpl.StockReceipt receipt = stockControlService.addMoreStock(newStock);
        Assertions.assertEquals("40.0", receipt.getCost());
    }

}

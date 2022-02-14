package com.uas.api.services;

import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Orders;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.OrdersRepository;
import com.uas.api.requests.MoreStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockControlServiceImpl implements StockControlService {
    private final LocationRepository locationRepository;
    private final OrdersRepository ordersRepository;
    @Autowired
    public StockControlServiceImpl(LocationRepository locationRepository, OrdersRepository ordersRepository) {
        this.locationRepository = locationRepository;
        this.ordersRepository = ordersRepository;
    }

    public boolean addMoreStock(MoreStockRequest moreStockRequest) {
        Location orderLocation = null;
        Optional<Location> orderLocationOpt = locationRepository.findLocationByLocationName(moreStockRequest.getLocation());
        if (orderLocationOpt.isPresent()) {
            orderLocation = orderLocationOpt.get();
        } else {
            //TODO: throw exception.
        }
        Orders newOrder = new Orders(orderLocation, moreStockRequest.getCost(), moreStockRequest.getOrderDate());
        ordersRepository.save(newOrder);
        return true;
    }
}

package com.uas.api.services;

import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Orders;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.StockToOrders;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.OrdersRepository;
import com.uas.api.repositories.PartTypeRepository;
import com.uas.api.repositories.StockToOrdersRepository;
import com.uas.api.requests.MoreStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class StockControlServiceImpl implements StockControlService {
    private final LocationRepository locationRepository;
    private final OrdersRepository ordersRepository;
    private final PartTypeRepository partTypeRepository;
    private final StockToOrdersRepository stockToOrdersRepository;
    @Autowired
    public StockControlServiceImpl(LocationRepository locationRepository, OrdersRepository ordersRepository, PartTypeRepository partTypeRepository, StockToOrdersRepository stockToOrdersRepository) {
        this.locationRepository = locationRepository;
        this.ordersRepository = ordersRepository;
        this.partTypeRepository = partTypeRepository;
        this.stockToOrdersRepository = stockToOrdersRepository;
    }

    public boolean addMoreStock(MoreStockRequest moreStockRequest) {
        Location orderLocation = null;
        ArrayList<Long> partTypes = moreStockRequest.getPartTypes();
        ArrayList<Integer> quantities = moreStockRequest.getQuantities();
        Optional<Location> orderLocationOpt = locationRepository.findLocationByLocationName(moreStockRequest.getLocation());
        if (orderLocationOpt.isPresent()) {
            orderLocation = orderLocationOpt.get();
        } else {
            //TODO: throw exception.
        }
        if (partTypes.size() != quantities.size()) {
            throw new IndexOutOfBoundsException("Missing quantity for part type in order!");
        }
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = Timestamp.valueOf(orderTime.format(dtf));
        Orders newOrder = new Orders(orderLocation, moreStockRequest.getCost(), ts);
        ordersRepository.save(newOrder);
        for (int i = 0; i < partTypes.size(); i++) {
            long part = partTypes.get(i);
            //This works if the sections in part type are not enums, doesn't work otherwise
            PartType partType = partTypeRepository.findPartTypeById(part);
            int quantity = quantities.get(i);
            StockToOrders newStockToOrder = new StockToOrders(newOrder, partType, quantity);
            stockToOrdersRepository.save(newStockToOrder);
        }
        return true;
    }
}

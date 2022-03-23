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
    /**
     * Repository for communication between api and location table.
     */
    private final LocationRepository locationRepository;
    /**
     * Repository for communication between api and orders table.
     */
    private final OrdersRepository ordersRepository;
    /**
     * Repository for communication between api and part type table.
     */
    private final PartTypeRepository partTypeRepository;
    /**
     * Repository for communication between api and stock to order table.
     */
    private final StockToOrdersRepository stockToOrdersRepository;

    /**
     * Constructor.
     * @param locationRepository required.
     * @param ordersRepository required.
     * @param partTypeRepository required.
     * @param stockToOrdersRepository required.
     */
    @Autowired
    public StockControlServiceImpl(final LocationRepository locationRepository, final OrdersRepository ordersRepository, final PartTypeRepository partTypeRepository, final StockToOrdersRepository stockToOrdersRepository) {
        this.locationRepository = locationRepository;
        this.ordersRepository = ordersRepository;
        this.partTypeRepository = partTypeRepository;
        this.stockToOrdersRepository = stockToOrdersRepository;
    }

    /**
     * Add more stock when requested.
     * @param moreStockRequest request body from controller.
     * @return true or false for success.
     */
    public boolean addMoreStock(final MoreStockRequest moreStockRequest) {
        Location orderLocation = null;
        ArrayList<Long> partTypes = moreStockRequest.getPartTypes();
        ArrayList<Integer> quantities = moreStockRequest.getQuantities();
        Optional<Location> orderLocationOpt = locationRepository.findLocationByLocationName(moreStockRequest.getLocation());
        if (orderLocationOpt.isPresent()) {
            orderLocation = orderLocationOpt.get();
        } else {
            throw new IllegalArgumentException("Location does not exist!");
        }
        if (partTypes.size() != quantities.size()) {
            throw new IndexOutOfBoundsException("Missing quantity for part type in order!");
        }
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = Timestamp.valueOf(orderTime.format(dtf));
        Orders newOrder = new Orders(orderLocation, moreStockRequest.getCost(),moreStockRequest.getSupplierEmail(), ts);
        ordersRepository.save(newOrder);
        for (int i = 0; i < partTypes.size(); i++) {
            long part = partTypes.get(i);
            PartType partType = partTypeRepository.findPartTypeById(part);
            int quantity = quantities.get(i);
            StockToOrders newStockToOrder = new StockToOrders(newOrder, partType, quantity);
            stockToOrdersRepository.save(newStockToOrder);
        }
        return true;
    }
}

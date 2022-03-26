package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
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
     * Service used to generate and send part invoices.
     */
    private final InvoiceService invoiceService;

    /**
     * Constructor.
     * @param locationRepository required.
     * @param ordersRepository required.
     * @param partTypeRepository required.
     * @param stockToOrdersRepository required.
     * @param invoiceService Service used to generate and send part invoices.
     */
    @Autowired
    public StockControlServiceImpl(final LocationRepository locationRepository, final OrdersRepository ordersRepository, final PartTypeRepository partTypeRepository, final StockToOrdersRepository stockToOrdersRepository, final InvoiceService invoiceService) {
        this.locationRepository = locationRepository;
        this.ordersRepository = ordersRepository;
        this.partTypeRepository = partTypeRepository;
        this.stockToOrdersRepository = stockToOrdersRepository;
        this.invoiceService = invoiceService;
    }

    /**
     * Add more stock when requested.
     * @param moreStockRequest request body from controller.
     * @return stock order.
     */
    public StockReceipt addMoreStock(final MoreStockRequest moreStockRequest) {
        StockReceipt reciept = null;
        ArrayList<Long> partTypes = moreStockRequest.getPartTypes();
        ArrayList<Integer> quantities = moreStockRequest.getQuantities();
        Location orderLocation = checkLocation(moreStockRequest.getLocation());
        checkPartTypesAndQuantities(partTypes, quantities);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timestamp ts = Timestamp.valueOf(orderTime.format(dtf));

        Orders newOrder = new Orders(orderLocation,  0, moreStockRequest.getSupplierEmail(), ts);
        ordersRepository.save(newOrder);
        double totalCost = 0;
        for (int i = 0; i < partTypes.size(); i++) {
            long part = partTypes.get(i);
            Optional<PartType> partType = partTypeRepository.findPartTypeById(part);
            int quantity = quantities.get(i);
            StockToOrders newStockToOrder = new StockToOrders(newOrder, partType.get(), quantity);
            stockToOrdersRepository.save(newStockToOrder);

            totalCost += partType.get().getPrice().doubleValue() * quantity;
        }

        newOrder.setTotalCost(totalCost);
        ordersRepository.save(newOrder);

        //passes order to invoice service to generate invoice.
        Orders invoiceOrder = ordersRepository.findByAttributes(moreStockRequest.getLocation(), moreStockRequest.getSupplierEmail());
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceData(invoiceOrder);
        String fileName = invoiceService.generatePDF(invoiceDTO);

        try {
            invoiceService.emailInvoice(fileName, moreStockRequest.getSupplierEmail());
        } catch (Exception e) {
            System.out.println(e);
        }

        reciept = new StockReceipt(String.valueOf(newOrder.getTotalCost()));
        return reciept;

    }

    /**
     * Checks that the location is valid.
     * @param location
     * @return the location if it is valid.
     */
    private Location checkLocation(final String location) {
        Location orderLocation = null;
        Optional<Location> orderLocationOpt = locationRepository.findLocationByLocationName(location);
        if (orderLocationOpt.isPresent()) {
            orderLocation = orderLocationOpt.get();
            return orderLocation;
        } else {
            throw new IllegalArgumentException("Location does not exist!");
        }
    }

    /**
     * Checks part types and quantities are the same length.
     * @param partTypes
     * @param quantities
     */
    private void checkPartTypesAndQuantities(final ArrayList<Long> partTypes, final ArrayList<Integer> quantities) {
        if (partTypes.size() != quantities.size()) {
            throw new IndexOutOfBoundsException("Missing quantity for part type in order!");
        }
    }

    static class StockReceipt {
        /**
         * The cost of the order.
         */
        private final String cost;

        StockReceipt(final String cost) {
            this.cost = cost;
        }

        public String getCost() {
            return cost;
        }

    }
}

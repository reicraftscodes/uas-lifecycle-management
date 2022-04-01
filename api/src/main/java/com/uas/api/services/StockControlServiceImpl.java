package com.uas.api.services;

import com.uas.api.models.dtos.InvoiceDTO;
import com.uas.api.models.dtos.StockOrderDTO;
import com.uas.api.models.entities.*;
import com.uas.api.repositories.*;
import com.uas.api.requests.MoreStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final PartRepository partRepository;
    /**
     * Repository for communication between api and stock to order table.
     */
    private final StockToOrdersRepository stockToOrdersRepository;
    /**
     * Service for generating and sending invoices.
     */
    private final InvoiceService invoiceService;

    /**
     * Constructor.
     * @param locationRepository required.
     * @param ordersRepository required.
     * @param partRepository required.
     * @param stockToOrdersRepository required.
     * @param invoiceService required.
     */
    @Autowired
    public StockControlServiceImpl(final LocationRepository locationRepository, final OrdersRepository ordersRepository, final PartRepository partRepository, final StockToOrdersRepository stockToOrdersRepository, final InvoiceService invoiceService) {
        this.locationRepository = locationRepository;
        this.ordersRepository = ordersRepository;
        this.partRepository = partRepository;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        double totalCost = 0;

        ArrayList<Long> partTypes = moreStockRequest.getPartIDs();
        ArrayList<Integer> quantities = moreStockRequest.getQuantities();
        Location orderLocation = checkLocation(moreStockRequest.getLocation());
        LocalDateTime orderTime = LocalDateTime.now();
        Timestamp ts = Timestamp.valueOf(orderTime.format(dtf));

        checkPartTypesAndQuantities(partTypes, quantities);

        Orders newOrder = new Orders(orderLocation, moreStockRequest.getSupplierEmail(), 0, ts);
        ordersRepository.save(newOrder);

        for (int i = 0; i < partTypes.size(); i++) {
            long part = partTypes.get(i);
            Optional<Part> partType = partRepository.findPartBypartNumber(part);
            int quantity = quantities.get(i);
            StockToOrders newStockToOrder = new StockToOrders(newOrder, partType.get(), quantity);
            stockToOrdersRepository.save(newStockToOrder);
            totalCost += partType.get().getPrice().doubleValue() * quantity;
        }
        newOrder.setTotalCost(totalCost);
        ordersRepository.save(newOrder);

        InvoiceDTO invoiceDTO = invoiceService.getInvoiceData(newOrder);
        String fileLocation = invoiceService.generatePDF(invoiceDTO);
        if (!Objects.equals(fileLocation, "error")) {
            invoiceService.emailInvoice(fileLocation, moreStockRequest.getSupplierEmail());
        }

        reciept = new StockReceipt(String.valueOf(totalCost));
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
     * Get a list of all part stock orders.
     * @return a list of stock order dtos.
     */
    public List<StockOrderDTO> getAllPreviousStockOrders() {
        List<StockOrderDTO> stockOrderDTOs = new ArrayList<>();
        List<StockToOrders> stockToOrders = stockToOrdersRepository.findAll();
        for (StockToOrders sto : stockToOrders) {
            StockOrderDTO stockOrderDTO = new StockOrderDTO(
                            sto.getOrderID().getLocationName().getLocationName(),
                            sto.getOrderID().getSupplierEmail(),
                            sto.getOrderID().getTotalCost(),
                            sto.getOrderID().getOrderDateTime(),
                            sto.getPartID().getPartType().getPartName().getName(),
                            sto.getQuantity());
            stockOrderDTOs.add(stockOrderDTO);
        }
        return stockOrderDTOs;
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

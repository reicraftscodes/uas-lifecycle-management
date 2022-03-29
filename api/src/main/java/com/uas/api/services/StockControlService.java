package com.uas.api.services;

import com.uas.api.models.dtos.StockOrderDTO;
import com.uas.api.requests.MoreStockRequest;

import java.util.List;

public interface StockControlService {
    /**
     * Add more stock.
     * @param moreStockRequest request body from controller.
     * @return the stock order.
     */
    StockControlServiceImpl.StockReceipt addMoreStock(MoreStockRequest moreStockRequest);

    List<StockOrderDTO> getAllPreviousStockOrders();
}

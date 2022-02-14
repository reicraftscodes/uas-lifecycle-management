package com.uas.api.services;

import com.uas.api.requests.MoreStockRequest;

public interface StockControlService {
    /**
     * Add more stock.
     * @param moreStockRequest request body from controller.
     * @return true or false for saving stock.
     */
    boolean addMoreStock(MoreStockRequest moreStockRequest);
}

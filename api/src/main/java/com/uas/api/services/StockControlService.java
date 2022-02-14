package com.uas.api.services;

import com.uas.api.requests.MoreStockRequest;

public interface StockControlService {
    boolean addMoreStock(MoreStockRequest moreStockRequest);
}

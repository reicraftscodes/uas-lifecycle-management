package com.uas.api.services;

import com.uas.api.requests.MoreStockRequest;
import org.springframework.stereotype.Service;

@Service
public class StockControlServiceImpl implements StockControlService {

    public boolean addMoreStock(MoreStockRequest moreStockRequest) {
        return true;
    }
}

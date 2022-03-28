package com.uas.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoreStockRequest {
    /**
     * Location for stock request.
     */
    private String location;
    /**
     * Cost of order.
     */
    private double cost;
    /**
     * All the part types.
     */
    private ArrayList<Long> partTypes;
    /**
     * Quantities for part types.
     */
    private ArrayList<Integer> quantities;
}

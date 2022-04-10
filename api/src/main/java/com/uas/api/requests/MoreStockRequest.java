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
     * The supplier email where the order is being sent.
     */
    private String supplierEmail;
    /**
     * All the part names.
     */
    private ArrayList<String> partNames;
    /**
     * Quantities for part types.
     */
    private ArrayList<Integer> quantities;
}

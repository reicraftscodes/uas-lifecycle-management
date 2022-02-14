package com.uas.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoreStockRequest {
    private String location;
    private double cost;
    private ArrayList<Long> partTypes;
    private ArrayList<Integer> quantities;
}

package com.uas.api.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoreStockRequest {
    private String location;
    private double cost;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date orderDate;
    private ArrayList<Integer> partTypes;
    private ArrayList<Integer> quantities;
}

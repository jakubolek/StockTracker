package com.jakubolek.stocktracker.dto;

import lombok.Data;

@Data
public class StockSearchDto {
    private String symbol;
    private String name;
}

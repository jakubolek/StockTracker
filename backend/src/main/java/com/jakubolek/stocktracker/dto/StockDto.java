package com.jakubolek.stocktracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

    private Long id;
    private String symbol;
    private String name;
    private LocalDate purchaseDate;
    private Double purchasePrice;
    private Double quantity;
    private Double currentPrice;
    private Double profitOrLoss;
    private Double percentageChange;
    private Double price7DaysAgo;
    private Double price30DaysAgo;
    private Double profitOrLoss7Days;
    private Double percentageChange7Days;
    private Double profitOrLoss30Days;
    private Double percentageChange30Days;
}

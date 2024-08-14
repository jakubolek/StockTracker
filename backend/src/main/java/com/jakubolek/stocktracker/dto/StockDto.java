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
    private LocalDate purchaseDate;
    private Double purchasePrice;
    private Double quantity;
    private Double currentPrice;
    private Double profitOrLoss;
    private Double percentageChange;
}

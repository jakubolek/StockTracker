package com.jakubolek.stocktracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSummary {
    private Double totalProfitOrLoss;
    private Double percentageChange;
}

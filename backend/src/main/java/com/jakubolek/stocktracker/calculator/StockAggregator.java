package com.jakubolek.stocktracker.calculator;

import com.jakubolek.stocktracker.dto.StockDto;
import org.springframework.stereotype.Component;

@Component
public class StockAggregator {

    public StockDto aggregate(StockDto stockDto1, StockDto stockDto2) {
        Double totalQuantity = stockDto1.getQuantity() + stockDto2.getQuantity();
        Double averagePurchasePrice = ((stockDto1.getPurchasePrice() * stockDto1.getQuantity()) + (stockDto2.getPurchasePrice() * stockDto2.getQuantity())) / totalQuantity;

        Double totalCurrentPrice = ((stockDto1.getCurrentPrice() * stockDto1.getQuantity()) + (stockDto2.getCurrentPrice() * stockDto2.getQuantity())) / totalQuantity;

        Double totalProfitOrLoss = (totalCurrentPrice - averagePurchasePrice) * totalQuantity;
        Double totalPercentageChange = ((totalCurrentPrice - averagePurchasePrice) / averagePurchasePrice) * 100;

        return StockDto.builder()
                .symbol(stockDto1.getSymbol())
                .quantity(totalQuantity)
                .purchasePrice(averagePurchasePrice)
                .currentPrice(totalCurrentPrice)
                .profitOrLoss(totalProfitOrLoss)
                .percentageChange(totalPercentageChange)
                .build();
    }
}

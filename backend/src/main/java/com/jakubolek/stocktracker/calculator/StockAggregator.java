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

        Double price7DaysAgo = stockDto1.getPrice7DaysAgo() != null ? stockDto1.getPrice7DaysAgo() : stockDto2.getPrice7DaysAgo();
        Double price30DaysAgo = stockDto1.getPrice30DaysAgo() != null ? stockDto1.getPrice30DaysAgo() : stockDto2.getPrice30DaysAgo();

        Double profitOrLoss7Days = (totalCurrentPrice - (price7DaysAgo != null ? price7DaysAgo : averagePurchasePrice)) * totalQuantity;
        Double profitOrLoss30Days = (totalCurrentPrice - (price30DaysAgo != null ? price30DaysAgo : averagePurchasePrice)) * totalQuantity;

        Double percentageChange7Days = price7DaysAgo != null ? ((totalCurrentPrice - price7DaysAgo) / price7DaysAgo) * 100 : null;
        Double percentageChange30Days = price30DaysAgo != null ? ((totalCurrentPrice - price30DaysAgo) / price30DaysAgo) * 100 : null;


        return StockDto.builder()
                .symbol(stockDto1.getSymbol())
                .quantity(totalQuantity)
                .purchasePrice(averagePurchasePrice)
                .currentPrice(totalCurrentPrice)
                .profitOrLoss(totalProfitOrLoss)
                .percentageChange(totalPercentageChange)
                .price7DaysAgo(price7DaysAgo)
                .price30DaysAgo(price30DaysAgo)
                .profitOrLoss7Days(profitOrLoss7Days)
                .profitOrLoss30Days(profitOrLoss30Days)
                .percentageChange7Days(percentageChange7Days)
                .percentageChange30Days(percentageChange30Days)
                .build();
    }
}

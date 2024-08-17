package com.jakubolek.stocktracker.validation;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.helper.StockPriceHelper;
import com.jakubolek.stocktracker.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StockValidator {
    private final StockPriceHelper stockPriceHelper;
    private final StockPriceService stockPriceService;

    public void validateStockSymbol(String symbol) {
        Map<LocalDate, Double> historicalPrices = stockPriceHelper.fetchPricesFromExternalService(symbol);

        LocalDate today = LocalDate.now();
        Double currentPrice = historicalPrices.get(today);
        Double price7DaysAgo = historicalPrices.get(today.minusDays(7));
        Double price30DaysAgo = historicalPrices.get(today.minusDays(30));

        if (currentPrice != null) {
            stockPriceService.fetchAndSaveStockPrice(
                    StockDto.builder().symbol(symbol).build(),
                    currentPrice
            );
        }

        if (price7DaysAgo != null) {
            stockPriceService.fetchAndSaveStockPrice(
                    StockDto.builder().symbol(symbol).build(),
                    price7DaysAgo,
                    LocalDate.now().minusDays(7)
            );
        }

        if (price30DaysAgo != null) {
            stockPriceService.fetchAndSaveStockPrice(
                    StockDto.builder().symbol(symbol).build(),
                    price30DaysAgo,
                    LocalDate.now().minusDays(30)
            );
        }
    }
}
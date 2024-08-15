package com.jakubolek.stocktracker.validation;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.helper.StockPriceHelper;
import com.jakubolek.stocktracker.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class StockValidator {
    private final StockPriceHelper stockPriceHelper;
    private final StockPriceService stockPriceService;

    public void validateStockSymbol(String symbol) {
        BigDecimal currentPrice = stockPriceHelper.fetchCurrentPriceFromExternalService(symbol);

        stockPriceService.fetchAndSaveStockPrice(
                StockDto.builder().symbol(symbol).build(),
                currentPrice
        );
    }
}
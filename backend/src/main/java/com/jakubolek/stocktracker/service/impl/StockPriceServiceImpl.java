package com.jakubolek.stocktracker.service.impl;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.StockPrice;
import com.jakubolek.stocktracker.repository.StockPriceRepository;
import com.jakubolek.stocktracker.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockPriceServiceImpl implements StockPriceService {

    private final StockPriceRepository stockPriceRepository;

    @Override
    public Map<String, StockPrice> fetchLatestPricesForSymbols(List<StockDto> stocks) {
        List<String> symbols = stocks.stream().map(StockDto::getSymbol).distinct().collect(Collectors.toList());
        List<StockPrice> prices = stockPriceRepository.findBySymbolInOrderByPriceDateDesc(symbols);

        Map<String, StockPrice> latestPrices = new HashMap<>();
        prices.forEach(price -> latestPrices.putIfAbsent(price.getSymbol(), price));

        latestPrices.entrySet().removeIf(entry -> !entry.getValue().getPriceDate().isEqual(LocalDate.now()));

        return latestPrices;
    }

    @Override
    public void fetchAndSaveStockPrice(StockDto stock, BigDecimal currentPrice) {
        if (currentPrice != null) {
            StockPrice stockPrice = StockPrice.builder()
                    .symbol(stock.getSymbol())
                    .name(stock.getName())
                    .price(currentPrice.doubleValue())
                    .priceDate(LocalDate.now())
                    .build();

            stockPriceRepository.save(stockPrice);
        } else {
            throw new IllegalArgumentException("Failed to fetch price for: " + stock.getSymbol());
        }
    }
}
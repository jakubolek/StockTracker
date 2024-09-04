package com.jakubolek.stocktracker.service.impl;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.StockPrice;
import com.jakubolek.stocktracker.repository.StockPriceRepository;
import com.jakubolek.stocktracker.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Map<String, StockPrice> getPricesForSymbolsOnDate(List<StockDto> stocks, LocalDate date) {
        List<String> symbols = stocks.stream().map(StockDto::getSymbol).distinct().collect(Collectors.toList());
        List<StockPrice> prices = stockPriceRepository.findBySymbolInAndPriceDate(symbols, date);

        Map<String, StockPrice> pricesOnDate = new HashMap<>();
        prices.forEach(price -> pricesOnDate.put(price.getSymbol(), price));

        return pricesOnDate;
    }

    @Override
    public void fetchAndSaveStockPrice(StockDto stock, Double price) {
        fetchAndSaveStockPrice(stock, price, LocalDate.now());
    }

    @Override
    public void fetchAndSaveStockPrice(StockDto stock, Double currentPrice, LocalDate date) {
        if (currentPrice != null) {
            if (!stockPriceRepository.existsBySymbolAndPriceDate(stock.getSymbol().toUpperCase(), date)) {
                StockPrice stockPrice = StockPrice.builder()
                        .symbol(stock.getSymbol())
                        .price(currentPrice)
                        .priceDate(date)
                        .build();

                stockPriceRepository.save(stockPrice);
            }
        } else {
            throw new IllegalArgumentException("Failed to fetch price for: " + stock.getSymbol());
        }
    }
}
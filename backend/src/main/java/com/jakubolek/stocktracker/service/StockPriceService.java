package com.jakubolek.stocktracker.service;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.StockPrice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StockPriceService {

    Map<String, StockPrice> getLatestPricesForSymbols(List<StockDto> stocks);

    Map<String, StockPrice> getPricesForSymbolsOnDate(List<StockDto> stocks, LocalDate date);


    void fetchAndSaveStockPrice(StockDto stock, Double currentPrice);

    void fetchAndSaveStockPrice(StockDto stock, Double currentPrice, LocalDate date);
}
package com.jakubolek.stocktracker.service;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.StockPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface StockPriceService {

    Map<String, StockPrice> fetchLatestPricesForSymbols(List<StockDto> stocks);

    void fetchAndSaveStockPrice(StockDto stock, BigDecimal currentPrice);
}
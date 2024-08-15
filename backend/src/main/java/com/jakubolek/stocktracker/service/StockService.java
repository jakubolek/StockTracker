package com.jakubolek.stocktracker.service;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.PortfolioSummary;

import java.util.List;

public interface StockService {

    StockDto addStock(StockDto stockDto);

    List<StockDto> getAllStocks();

    List<StockDto> getTransactions();

    void deleteStock(Long id);

    List<StockDto> getAggregatedReport();

    PortfolioSummary calculatePortfolioSummary();
}

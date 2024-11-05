package com.jakubolek.stocktracker.repository;

import com.jakubolek.stocktracker.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findBySymbolContainingOrNameContaining(String symbol, String name);
}

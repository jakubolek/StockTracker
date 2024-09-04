package com.jakubolek.stocktracker.repository;

import com.jakubolek.stocktracker.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    List<StockPrice> findBySymbolInAndPriceDate(List<String> symbols, LocalDate date);

    boolean existsBySymbolAndPriceDate(String symbol, LocalDate priceDate);
}

package com.jakubolek.stocktracker.repository;

import com.jakubolek.stocktracker.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}

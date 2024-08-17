package com.jakubolek.stocktracker.mapper;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public Stock toEntity(StockDto stockDto) {
        return Stock.builder()
                .symbol(stockDto.getSymbol().toUpperCase())
                .name(stockDto.getName())
                .purchaseDate(stockDto.getPurchaseDate())
                .purchasePrice(stockDto.getPurchasePrice())
                .quantity(stockDto.getQuantity())
                .build();
    }

    public StockDto toDto(Stock stock) {
        return StockDto.builder()
                .id(stock.getId())
                .symbol(stock.getSymbol().toUpperCase())
                .name(stock.getName())
                .purchaseDate(stock.getPurchaseDate())
                .purchasePrice(stock.getPurchasePrice())
                .quantity(stock.getQuantity())
                .build();
    }
}
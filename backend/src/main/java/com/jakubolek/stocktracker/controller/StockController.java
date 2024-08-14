package com.jakubolek.stocktracker.controller;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@AllArgsConstructor
public class StockController {

    private StockService stockService;

    @PostMapping
    public ResponseEntity<StockDto> addStock(@RequestBody StockDto stockDto) {
        return ResponseEntity.status(201).body(stockService.addStock(stockDto));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<StockDto>> getTransactions() throws IOException {
        return ResponseEntity.ok(stockService.getTransactions());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aggregated-report")
    public ResponseEntity<List<StockDto>>  getAggregatedReport() throws IOException {
        return ResponseEntity.ok(stockService.getAggregatedReport());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
}

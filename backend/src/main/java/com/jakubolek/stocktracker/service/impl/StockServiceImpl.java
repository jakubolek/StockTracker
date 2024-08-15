package com.jakubolek.stocktracker.service.impl;

import com.jakubolek.stocktracker.calculator.StockAggregator;
import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.mapper.StockMapper;
import com.jakubolek.stocktracker.model.PortfolioSummary;
import com.jakubolek.stocktracker.model.Stock;
import com.jakubolek.stocktracker.model.StockPrice;
import com.jakubolek.stocktracker.repository.StockRepository;
import com.jakubolek.stocktracker.service.StockPriceService;
import com.jakubolek.stocktracker.service.StockService;
import com.jakubolek.stocktracker.validation.StockValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockPriceService stockPriceService;
    private final StockMapper stockMapper;
    private final StockAggregator stockAggregator;
    private final StockValidator stockValidator;

    @Override
    public StockDto addStock(StockDto stockDto) {
        stockValidator.validateStockSymbol(stockDto.getSymbol());
        Stock savedStock = stockRepository.save(stockMapper.toEntity(stockDto));
        return stockMapper.toDto(savedStock);
    }

    @Override
    public List<StockDto> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockDto> getTransactions() {
        List<StockDto> stocks = getAllStocks();
        Map<String, StockPrice> latestPrices = stockPriceService.fetchLatestPricesForSymbols(stocks);

        stocks.forEach(stock -> {
            StockPrice latestPrice = latestPrices.get(stock.getSymbol());
            if (latestPrice != null) {
                stock.setCurrentPrice(latestPrice.getPrice());
                stock.setProfitOrLoss((latestPrice.getPrice() - stock.getPurchasePrice()) * stock.getQuantity());
                stock.setPercentageChange(((latestPrice.getPrice() - stock.getPurchasePrice()) / stock.getPurchasePrice()) * 100);
            }
        });

        return stocks;
    }

    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public List<StockDto> getAggregatedReport() {
        List<StockDto> transactions = getTransactions();

        Map<String, StockDto> aggregatedStocks = transactions.stream()
                .collect(Collectors.groupingBy(StockDto::getSymbol,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                stockList -> stockList.stream()
                                        .reduce(stockAggregator::aggregate)
                                        .orElseThrow(() -> new IllegalArgumentException("Failed to aggregate stock data"))
                        )
                ));

        transactions.stream()
                .collect(Collectors.groupingBy(StockDto::getSymbol, LinkedHashMap::new,
                        Collectors.minBy(Comparator.comparing(StockDto::getId))))
                .forEach((symbol, optionalStock) -> optionalStock.ifPresent(stock -> aggregatedStocks.get(symbol).setName(stock.getName())));

        return new ArrayList<>(aggregatedStocks.values());
    }

    @Override
    public PortfolioSummary calculatePortfolioSummary() {
        List<StockDto> transactions = getTransactions();

        Double totalInvestment = transactions.stream()
                .mapToDouble(stock -> stock.getPurchasePrice() * stock.getQuantity())
                .sum();

        Double totalCurrentValue = transactions.stream()
                .mapToDouble(stock -> stock.getCurrentPrice() * stock.getQuantity())
                .sum();

        Double totalProfitOrLoss = totalCurrentValue - totalInvestment;
        Double percentageChange = totalInvestment > 0 ? (totalProfitOrLoss / totalInvestment) * 100 : 0;

        return new PortfolioSummary(totalProfitOrLoss, percentageChange);
    }

}

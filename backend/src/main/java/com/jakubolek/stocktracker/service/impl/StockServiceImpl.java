package com.jakubolek.stocktracker.service.impl;

import com.jakubolek.stocktracker.calculator.StockAggregator;
import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.helper.StockPriceHelper;
import com.jakubolek.stocktracker.mapper.StockMapper;
import com.jakubolek.stocktracker.model.Stock;
import com.jakubolek.stocktracker.model.StockPrice;
import com.jakubolek.stocktracker.repository.StockRepository;
import com.jakubolek.stocktracker.service.StockPriceService;
import com.jakubolek.stocktracker.service.StockService;
import com.jakubolek.stocktracker.validation.StockValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final StockPriceHelper stockPriceHelper;

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
        LocalDate today = LocalDate.now();

        // Pobierz ceny z bazy danych
        Map<String, StockPrice> latestPrices = stockPriceService.getLatestPricesForSymbols(stocks);
        Map<String, StockPrice> prices7DaysAgo = stockPriceService.getPricesForSymbolsOnDate(stocks, today.minusDays(7));
        Map<String, StockPrice> prices30DaysAgo = stockPriceService.getPricesForSymbolsOnDate(stocks, today.minusDays(30));

        stocks.forEach(stock -> {
            String symbol = stock.getSymbol().toUpperCase();

            Double currentPrice = Optional.ofNullable(latestPrices.get(symbol))
                    .map(StockPrice::getPrice)
                    .orElseGet(() -> {
                        Map<LocalDate, Double> historicalPrices = stockPriceHelper.fetchPricesFromExternalService(symbol);
                        Double fetchedPrice = historicalPrices.get(today);

                        stockPriceService.fetchAndSaveStockPrice(
                                StockDto.builder().symbol(symbol).build(),
                                fetchedPrice
                        );

                        historicalPrices.forEach((date, price) -> {
                            if (date.equals(today.minusDays(7))) {
                                stockPriceService.fetchAndSaveStockPrice(
                                        StockDto.builder().symbol(symbol).build(),
                                        price,
                                        date
                                );
                            } else if (date.equals(today.minusDays(30))) {
                                stockPriceService.fetchAndSaveStockPrice(
                                        StockDto.builder().symbol(symbol).build(),
                                        price,
                                        date
                                );
                            }
                        });

                        return fetchedPrice;
                    });

            stock.setCurrentPrice(currentPrice);

            Double price7DaysAgo = Optional.ofNullable(prices7DaysAgo.get(symbol))
                    .map(StockPrice::getPrice)
                    .orElseGet(() -> stockPriceHelper.fetchPricesFromExternalService(symbol).get(today.minusDays(7)));

            Double price30DaysAgo = Optional.ofNullable(prices30DaysAgo.get(symbol))
                    .map(StockPrice::getPrice)
                    .orElseGet(() -> stockPriceHelper.fetchPricesFromExternalService(symbol).get(today.minusDays(30)));

            stock.setPrice7DaysAgo(price7DaysAgo);
            stock.setPrice30DaysAgo(price30DaysAgo);

            if (currentPrice != null) {
                stock.setProfitOrLoss((currentPrice - stock.getPurchasePrice()) * stock.getQuantity());
                stock.setPercentageChange(((currentPrice - stock.getPurchasePrice()) / stock.getPurchasePrice()) * 100);
            }

            if (price7DaysAgo != null) {
                stock.setProfitOrLoss7Days((currentPrice - price7DaysAgo) * stock.getQuantity());
                stock.setPercentageChange7Days(((currentPrice - price7DaysAgo) / price7DaysAgo) * 100);
            }

            if (price30DaysAgo != null) {
                stock.setProfitOrLoss30Days((currentPrice - price30DaysAgo) * stock.getQuantity());
                stock.setPercentageChange30Days(((currentPrice - price30DaysAgo) / price30DaysAgo) * 100);
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

}

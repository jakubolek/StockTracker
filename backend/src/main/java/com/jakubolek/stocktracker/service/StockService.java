package com.jakubolek.stocktracker.service;

import com.jakubolek.stocktracker.calculator.StockAggregator;
import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.mapper.StockMapper;
import com.jakubolek.stocktracker.model.Stock;
import com.jakubolek.stocktracker.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final StockAggregator stockAggregator;

    @Value("${stooq.api.base-url}")
    private String stooqBaseUrl;

    public StockDto addStock(StockDto stockDto) throws IllegalArgumentException {
        validateStockSymbol(stockDto.getSymbol());
        Stock savedStock = stockRepository.save(stockMapper.toEntity(stockDto));
        return stockMapper.toDto(savedStock);
    }

    public List<StockDto> getAllStocks() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StockDto> getTransactions() throws IOException {
        List<StockDto> stocks = getAllStocks();

        for (StockDto stock : stocks) {
            BigDecimal currentPrice = fetchCurrentPriceFromStooq(buildStooqUrl(stock.getSymbol()));

            if (currentPrice != null) {
                stock.setCurrentPrice(currentPrice.doubleValue());

                double profitOrLoss = (currentPrice.doubleValue() - stock.getPurchasePrice()) * stock.getQuantity();
                stock.setProfitOrLoss(BigDecimal.valueOf(profitOrLoss).setScale(2, RoundingMode.HALF_UP).doubleValue());

                double percentageChange = ((currentPrice.doubleValue() - stock.getPurchasePrice()) / stock.getPurchasePrice()) * 100;
                stock.setPercentageChange(BigDecimal.valueOf(percentageChange).setScale(2, RoundingMode.HALF_UP).doubleValue());
            } else {
                throw new IOException("Failed to fetch current price for stock: " + stock.getSymbol());
            }
        }

        return stocks;
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    private String buildStooqUrl(String symbol) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        return String.format(stooqBaseUrl,
                symbol.toUpperCase(),
                startDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                endDate.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    private BigDecimal fetchCurrentPriceFromStooq(String urlStr) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String[] lines = in.lines().toArray(String[]::new);
                if (lines.length > 1) {
                    String[] lastLine = lines[lines.length - 1].split(",");
                    return new BigDecimal(lastLine[lastLine.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StockDto> getAggregatedReport() throws IOException {
        List<StockDto> transactions = getTransactions();

        Map<String, StockDto> aggregatedStocks = transactions.stream()
                .collect(Collectors.toMap(StockDto::getSymbol, Function.identity(), stockAggregator::aggregate));

        return new ArrayList<>(aggregatedStocks.values());
    }


    public void validateStockSymbol(String symbol) throws IllegalArgumentException {
        if (fetchCurrentPriceFromStooq(buildStooqUrl(symbol)) == null) throw new IllegalArgumentException("Invalid stock symbol: " + symbol);
    }

}

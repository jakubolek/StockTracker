package com.jakubolek.stocktracker.scheduler;

import com.jakubolek.stocktracker.helper.StockPriceHelper;
import com.jakubolek.stocktracker.model.StockPrice;
import com.jakubolek.stocktracker.repository.StockPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {

    private final StockPriceRepository stockPriceRepository;
    private final StockPriceHelper stockPriceHelper;

    @Scheduled(cron = "0 0 0,4,16,21 * * *")
    public void updateStockPrices() {
        LocalDate today = LocalDate.now();
        System.out.println("Updating stock prices: " + LocalDateTime.now());
        LocalDate date7DaysAgo = today.minusDays(7);
        LocalDate date30DaysAgo = today.minusDays(30);

        List<StockPrice> allStockPrices = stockPriceRepository.findAll();

        Set<String> symbols = allStockPrices.stream()
                .map(StockPrice::getSymbol)
                .collect(Collectors.toSet());

        for (String symbol : symbols) {
            Map<LocalDate, Double> stockPrices = stockPriceHelper.getStockPrices(symbol);

            for (StockPrice stockPrice : allStockPrices) {
                if (!stockPrice.getSymbol().equals(symbol)) {
                    continue;
                }

                LocalDate stockDate = stockPrice.getPriceDate();
                Double newPrice;

                if (stockDate.isEqual(today)) {
                    newPrice = stockPrices.get(today);
                } else if (stockDate.isEqual(date7DaysAgo)) {
                    newPrice = stockPrices.get(date7DaysAgo);
                } else if (stockDate.isEqual(date30DaysAgo)) {
                    newPrice = stockPrices.get(date30DaysAgo);
                } else {
                    LocalDate newDate = stockDate.plusDays(1);
                    newPrice = stockPrices.get(newDate);
                    stockPrice.setPriceDate(newDate);
                }

                if (newPrice != null) {
                    stockPrice.setPrice(newPrice);
                    stockPriceRepository.save(stockPrice);
                }
            }
        }
    }
}

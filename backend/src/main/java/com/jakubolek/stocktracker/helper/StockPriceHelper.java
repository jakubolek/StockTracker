package com.jakubolek.stocktracker.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StockPriceHelper {
    @Value("${stooq.api.base-url}")
    private String stooqBaseUrl;

    public Map<LocalDate, Double> fetchPricesFromExternalService(String symbol) {
        String urlStr = buildStooqUrl(symbol);
        Map<LocalDate, Double> prices = new HashMap<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("GET");

            try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String[] lines = in.lines().toArray(String[]::new);

                for (int i = 1; i < lines.length; i++) {
                    String[] line = lines[i].split(",");
                    LocalDate date = LocalDate.parse(line[0]);
                    Double closePrice = Double.parseDouble(line[4]);

                    prices.put(date, closePrice);
                }

                fillMissingDates(prices);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prices;
    }

    private String buildStooqUrl(String symbol) {
        return String.format(stooqBaseUrl,
                symbol.toUpperCase(),
                LocalDate.now().minusDays(31).format(DateTimeFormatter.BASIC_ISO_DATE),
                LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    private void fillMissingDates(Map<LocalDate, Double> prices) {
        LocalDate startDate = prices.keySet().stream()
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalStateException("No data available for the given symbol in the specified date range."));
        LocalDate endDate = LocalDate.now();

        Double lastKnownPrice = null;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!prices.containsKey(date)) {
                prices.put(date, lastKnownPrice);
            } else {
                lastKnownPrice = prices.get(date);
            }
        }
    }
}
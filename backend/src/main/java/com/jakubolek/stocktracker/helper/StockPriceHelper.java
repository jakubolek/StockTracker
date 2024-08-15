package com.jakubolek.stocktracker.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class StockPriceHelper {
    @Value("${stooq.api.base-url}")
    private String stooqBaseUrl;

    public BigDecimal fetchCurrentPriceFromExternalService(String symbol) {
        String urlStr = buildStooqUrl(symbol);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("GET");

            try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String[] lines = in.lines().toArray(String[]::new);
                if (lines.length > 1) {
                    String[] lastLine = lines[lines.length - 1].split(",");
                    return new BigDecimal(lastLine[lastLine.length - 1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildStooqUrl(String symbol) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        return String.format(stooqBaseUrl,
                symbol.toUpperCase(),
                startDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                endDate.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

}
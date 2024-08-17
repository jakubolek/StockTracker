package com.jakubolek.stocktracker.calculator;

import com.jakubolek.stocktracker.dto.StockDto;
import com.jakubolek.stocktracker.model.PortfolioSummary;
import com.jakubolek.stocktracker.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InvestmentSummaryCalculator {

    private final StockService stockService;

    public PortfolioSummary calculatePortfolioSummary() {
        List<StockDto> transactions = stockService.getTransactions();

        Double totalInvestment = transactions.stream()
                .mapToDouble(stock -> stock.getPurchasePrice() * stock.getQuantity())
                .sum();

        Double totalCurrentValue = transactions.stream()
                .mapToDouble(stock -> stock.getCurrentPrice() * stock.getQuantity())
                .sum();

        Double totalProfitOrLoss = totalCurrentValue - totalInvestment;
        Double percentageChange = totalInvestment > 0 ? (totalProfitOrLoss / totalInvestment) * 100 : 0;

        return new PortfolioSummary(totalProfitOrLoss, percentageChange, totalInvestment, totalCurrentValue);
    }
}

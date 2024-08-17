import React, {useEffect, useState} from 'react';
import {stockService} from '../services/StockService';
import '../css/PortfolioSummary.css';
import {PortfolioSummaryData} from "../model/PortfolioSummaryData";

const PortfolioSummary: React.FC = () => {
    const [summary, setSummary] = useState<PortfolioSummaryData | null>(null);

    useEffect(() => {
        fetchPortfolioSummary();
    }, []);

    const fetchPortfolioSummary = () => {
        stockService.getPortfolioSummary()
            .then((response) => {
                setSummary(response.data);
            })
            .catch((error) => {
                console.error('Error fetching portfolio summary:', error);
            });
    };

    const getValueClass = (value: number) => {
        return value >= 0 ? 'positive' : 'negative';
    };

    return (
        <div className="portfolio-summary">
            <h2>Portfolio Summary</h2>
            {summary ? (
                <div>
                    <p><strong>Investment: </strong> {summary.totalInvestment.toFixed(2)} PLN</p>
                    <p>
                        <strong>Value: </strong>
                        <span className={getValueClass(summary.totalCurrentValue - summary.totalInvestment)}>
                            {summary.totalCurrentValue.toFixed(2)} PLN
                        </span>
                    </p>
                    <p>
                        <strong>{summary.totalCurrentValue > summary.totalInvestment ? 'Profit' : 'Loss'}: </strong>
                        <span className={getValueClass(summary.totalProfitOrLoss)}>
                            {summary.totalProfitOrLoss.toFixed(2)} PLN
                        </span>
                    </p>
                    <p>
                        <strong>Change: </strong>
                        <span className={getValueClass(summary.percentageChange)}>
                            {summary.percentageChange.toFixed(2)}%
                        </span>
                    </p>
                </div>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
};

export default PortfolioSummary;
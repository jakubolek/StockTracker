import React, {useEffect, useState} from 'react';
import {stockService} from '../services/StockService';
import '../css/PortfolioSummary.css';
import {PortfolioSummaryData} from "../model/PortfolioSummaryData";

const PortfolioSummary: React.FC = () => {
    const [portfolioSummary, setPortfolioSummary] = useState<PortfolioSummaryData | null>(null);

    useEffect(() => {
        const fetchPortfolioSummary = async () => {
            const response = await stockService.getPortfolioSummary();
            if (response) {
                setPortfolioSummary(response.data);
            }
        };
        fetchPortfolioSummary();
    }, []);

    return (
        <div className="portfolio-summary">
            <h2>Portfolio Summary</h2>
            {portfolioSummary !== null && (
                <div>
                    <p className={portfolioSummary.totalProfitOrLoss >= 0 ? 'positive' : 'negative'}>
                        {portfolioSummary.totalProfitOrLoss >= 0 ? 'Profit' : 'Loss'}: {portfolioSummary.totalProfitOrLoss.toFixed(2)} PLN
                    </p>
                    <p className={portfolioSummary.percentageChange >= 0 ? 'positive' : 'negative'}>
                        {portfolioSummary.percentageChange >= 0 ? 'Profit' : 'Loss'}: {portfolioSummary.percentageChange.toFixed(2)}%
                    </p>
                </div>
            )}
        </div>
    );
};

export default PortfolioSummary;

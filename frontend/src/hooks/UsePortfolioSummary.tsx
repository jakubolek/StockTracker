import { useState, useEffect } from 'react';
import { stockService } from '../services/StockService';
import { PortfolioSummaryData } from "../model/PortfolioSummaryData";

export const usePortfolioSummary = () => {
    const [summary, setSummary] = useState<PortfolioSummaryData | null>(null);

    useEffect(() => {
        stockService.getPortfolioSummary()
            .then((response) => setSummary(response.data))
            .catch((error) => console.error('Error fetching portfolio summary:', error));
    }, []);

    return {
        summary,
        getValueClass: (value: number) => value >= 0 ? 'positive' : 'negative'
    };
};

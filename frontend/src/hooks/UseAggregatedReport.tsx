import { useState, useEffect } from 'react';
import { stockService } from '../services/StockService';
import { Stock } from "../model/Stock";

export const useAggregatedReport = () => {
    const [aggregatedReport, setAggregatedReport] = useState<Stock[]>([]);

    useEffect(() => {
        stockService.getAggregatedReport()
            .then(response => setAggregatedReport(response.data))
            .catch(error => console.error('Error fetching aggregated report:', error));
    }, []);

    return { aggregatedReport };
};

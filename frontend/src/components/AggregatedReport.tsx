import React, {useEffect, useState} from 'react';
import {stockService} from '../services/StockService';
import {Stock} from "../model/Stock";
import '../css/Reports.css';

const AggregatedReport: React.FC = () => {
    const [aggregatedReport, setAggregatedReport] = useState<Stock[]>([]);

    useEffect(() => {
        stockService.getAggregatedReport()
            .then(response => {
                if (response) {
                    setAggregatedReport(response.data);
                }
            })
            .catch(error => {
                console.error('Error fetching aggregated report:', error);
            });
    }, []);

    return (
        <div className="aggregated-report">
            <h2>Aggregated Stock Table</h2>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Symbol</th>
                    <th>Average Purchase Price</th>
                    <th>Total Quantity</th>
                    <th>Current Price</th>
                    <th>Change</th>
                    <th>%</th>
                    <th>7d</th>
                    <th>% 7d</th>
                    <th>30d</th>
                    <th>% 30d</th>
                </tr>
                </thead>
                <tbody>
                {aggregatedReport.map(stock => (
                    <tr key={stock.symbol}>
                        <td>{stock.name}</td>
                        <td>{stock.symbol}</td>
                        <td>{stock.purchasePrice.toFixed(2)}</td>
                        <td>{stock.quantity}</td>
                        <td>{stock.currentPrice?.toFixed(2)}</td>
                        <td className={stock.profitOrLoss && stock.profitOrLoss >= 0 ? 'positive' : 'negative'}>
                            {stock.profitOrLoss?.toFixed(2)} PLN
                        </td>
                        <td className={stock.percentageChange && stock.percentageChange >= 0 ? 'positive' : 'negative'}>
                            {stock.percentageChange?.toFixed(2)}%
                        </td>
                        <td className={stock.profitOrLoss7Days && stock.profitOrLoss7Days >= 0 ? 'positive' : 'negative'}>
                            {stock.profitOrLoss7Days?.toFixed(2)} PLN
                        </td>
                        <td className={stock.percentageChange7Days && stock.percentageChange7Days >= 0 ? 'positive' : 'negative'}>
                            {stock.percentageChange7Days?.toFixed(2)}%
                        </td>
                        <td className={stock.profitOrLoss30Days && stock.profitOrLoss30Days >= 0 ? 'positive' : 'negative'}>
                            {stock.profitOrLoss30Days?.toFixed(2)} PLN
                        </td>
                        <td className={stock.percentageChange30Days && stock.percentageChange30Days >= 0 ? 'positive' : 'negative'}>
                            {stock.percentageChange30Days?.toFixed(2)}%
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AggregatedReport;

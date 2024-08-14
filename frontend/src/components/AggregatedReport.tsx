import React, { useEffect, useState } from 'react';
import StockService from '../services/StockService';
import {Stock} from "../model/Stock";

const AggregatedReport: React.FC = () => {
    const [aggregatedReport, setAggregatedReport] = React.useState<Stock[]>([]);

    useEffect(() => {
        fetchAggregatedReport();
    }, []);

    const fetchAggregatedReport = () => {
        StockService.getAggregatedReport()
            .then((response: any) => {
                setAggregatedReport(response.data);
            })
            .catch((error: any) => {
                console.error('There was an error retrieving the report!', error);
            });
    };

    return (
        <div>
            <h2>Aggregated stock table</h2>
            <table>
                <thead>
                <tr>
                    <th>Symbol</th>
                    <th>Average Purchase Price</th>
                    <th>Total Quantity</th>
                    <th>Current Price</th>
                    <th>Profit/Loss</th>
                    <th>Percentage Change</th>
                </tr>
                </thead>
                <tbody>
                {aggregatedReport.map(stock => (
                    <tr key={stock.symbol}>
                        <td>{stock.symbol}</td>
                        <td>{stock.purchasePrice.toFixed(2)}</td>
                        <td>{stock.quantity}</td>
                        <td>{stock.currentPrice?.toFixed(2)}</td>
                        <td className={stock.profitOrLoss && stock.profitOrLoss >= 0 ? 'positive' : 'negative'}>
                            {stock.profitOrLoss?.toFixed(2)}
                        </td>
                        <td className={stock.percentageChange && stock.percentageChange >= 0 ? 'positive' : 'negative'}>
                            {stock.percentageChange?.toFixed(2)}%
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AggregatedReport;

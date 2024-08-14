import React from 'react';
import StockService from '../services/StockService';
import {Stock} from "../model/Stock";
import '../css/StockTransactions.css';

const StockTransactions: React.FC = () => {
    const [stocks, setStocks] = React.useState<Stock[]>([]);

    React.useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        StockService.getTransactions()
            .then((response: any) => {
                setStocks(response.data);
            })
            .catch((error: any) => {
                console.error('There was an error retrieving the report!', error);
            });
    };

    const handleDelete = (id: number) => {
        StockService.deleteStock(id)
            .then(() => {
                setStocks(stocks.filter(stock => stock.id !== id));
            })
            .catch((error: any) => {
                console.error('There was an error deleting the stock!', error);
            });
    };

    const formatPercentage = (value: number | undefined) => {
        return value !== undefined ? `${value.toFixed(2)}%` : "N/A";
    };

    const getProfitLossClass = (value: number | undefined) => {
        if (value !== undefined) {
            return value >= 0 ? 'positive' : 'negative';
        }
        return '';
    };

    return (
        <div className="stock-transactions">
            <h2>Transactions Table</h2>
            <table>
                <thead>
                <tr>
                    <th>Symbol</th>
                    <th>Purchase Date</th>
                    <th>Purchase Price</th>
                    <th>Quantity</th>
                    <th>Current Price</th>
                    <th>Profit/Loss</th>
                        <th>Percentage Change</th>
                        <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {stocks.map(stock => (
                    <tr key={stock.id}>
                        <td>{stock.symbol}</td>
                        <td>{stock.purchaseDate}</td>
                        <td>{stock.purchasePrice}</td>
                        <td>{stock.quantity}</td>
                        <td>{stock.currentPrice}</td>
                            <td className={getProfitLossClass(stock.profitOrLoss)}>
                                {stock.profitOrLoss?.toFixed(2)}
                            </td>
                            <td className={getProfitLossClass(stock.percentageChange)}>
                                {formatPercentage(stock.percentageChange)}
                            </td>
                            <td>
                                <button className="delete-button" onClick={() => handleDelete(stock.id!)}>Delete</button>
                            </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default StockTransactions;

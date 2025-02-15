import React, {useState, useEffect} from 'react';
import Select from 'react-select';
import {formatPercentage, getProfitLossClass} from '../utils/Utils';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {FaChevronLeft, FaChevronRight} from 'react-icons/fa';
import {useStockTransactions} from '../hooks/UseStockTransactions';
import '../css/Reports.css';

const StockTransactions: React.FC = () => {
    const {
        months,
        currentPage,
        currentMonth,
        currentItems,
        handleMonthChange,
        handleDelete,
        setCurrentPage
    } = useStockTransactions();

    return (
        <div className="stock-transactions">
            <h2>Transactions</h2>
            <div className="filters">
                <Select
                    className="select-dropdown"
                    options={months.map(month => ({value: month, label: month}))}
                    value={{value: currentMonth, label: currentMonth}}
                    onChange={handleMonthChange}
                />
            </div>
            <div className="pagination">
                <button
                    disabled={currentPage === 0}
                    onClick={() => setCurrentPage(currentPage - 1)}
                >
                    <FaChevronLeft/>
                </button>
                <button
                    disabled={currentPage === months.length - 1}
                    onClick={() => setCurrentPage(currentPage + 1)}
                >
                    <FaChevronRight/>
                </button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Symbol</th>
                    <th>Purchase Date</th>
                    <th>Purchase Price</th>
                    <th>Quantity</th>
                    <th>Current Price</th>
                    <th>Change</th>
                    <th>%</th>
                    <th>7d</th>
                    <th>% 7d</th>
                    <th>30d</th>
                    <th>% 30d</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {currentItems.map(stock => (
                    <tr key={stock.id}>
                        <td>{stock.name}</td>
                        <td>{stock.symbol}</td>
                        <td>{stock.purchaseDate}</td>
                        <td>{stock.purchasePrice}</td>
                        <td>{stock.quantity}</td>
                        <td>{stock.currentPrice}</td>
                        <td className={getProfitLossClass(stock.profitOrLoss)}>
                            {stock.profitOrLoss?.toFixed(2)} PLN
                        </td>
                        <td className={getProfitLossClass(stock.percentageChange)}>
                            {formatPercentage(stock.percentageChange)}
                        </td>
                        <td className={getProfitLossClass(stock.profitOrLoss7Days)}>
                            {stock.profitOrLoss7Days?.toFixed(2)} PLN
                        </td>
                        <td className={getProfitLossClass(stock.percentageChange7Days)}>
                            {formatPercentage(stock.percentageChange7Days)}
                        </td>
                        <td className={getProfitLossClass(stock.profitOrLoss30Days)}>
                            {stock.profitOrLoss30Days?.toFixed(2)} PLN
                        </td>
                        <td className={getProfitLossClass(stock.percentageChange30Days)}>
                            {formatPercentage(stock.percentageChange30Days)}
                        </td>
                        <td>
                            <button className="delete-button" onClick={() => handleDelete(stock.id!)}>
                                <FontAwesomeIcon icon={faTrash}/>
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default StockTransactions;

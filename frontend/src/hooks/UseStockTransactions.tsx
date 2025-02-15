import {useState, useEffect} from 'react';
import {stockService} from '../services/StockService';
import {Stock} from "../model/Stock";
import {groupByMonth} from '../utils/Utils';

export const useStockTransactions = () => {
    const [stocks, setStocks] = useState<Stock[]>([]);
    const [groupedStocks, setGroupedStocks] = useState<Record<string, Stock[]>>({});
    const [months, setMonths] = useState<string[]>([]);
    const [currentPage, setCurrentPage] = useState(0);

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        if (months.length > 0) {
            const currentMonth = new Date().toLocaleString('en-US', {month: 'long', year: 'numeric'});
            const index = months.indexOf(currentMonth);
            if (index !== -1) setCurrentPage(index);
        }
    }, [months]);

    const fetchData = () => {
        stockService.getTransactions()
            .then((response: any) => {
                setStocks(response.data);
                const grouped = groupByMonth(response.data);
                setGroupedStocks(grouped);
                const sortedMonths = Object.keys(grouped).sort((a, b) => new Date(b).getTime() - new Date(a).getTime());
                setMonths(sortedMonths.reverse());
            })
            .catch((error: any) => console.error('There was an error retrieving the report!', error));
    };

    const handleMonthChange = (selectedOption: any) => {
        const index = months.indexOf(selectedOption.value);
        if (index !== -1) setCurrentPage(index);
    };

    const handleDelete = (id: number) => {
        stockService.deleteStock(id)
            .then(() => {
                window.location.reload();
            })
            .catch((error: any) => console.error('There was an error deleting the stock!', error));
    };

    const currentMonth = months[currentPage];
    const currentItems = groupedStocks[currentMonth] || [];

    return {
        stocks,
        groupedStocks,
        months,
        currentPage,
        currentMonth,
        currentItems,
        fetchData,
        handleMonthChange,
        handleDelete,
        setCurrentPage,
    };
};

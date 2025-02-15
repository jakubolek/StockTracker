import {Stock} from "../model/Stock";

export const formatPercentage = (value: number | null | undefined) =>
    value != null ? `${value.toFixed(2)}%` : "N/A";

export const getProfitLossClass = (value: number | undefined) =>
    value !== undefined ? (value >= 0 ? 'positive' : 'negative') : '';

export const groupByMonth = (stocks: Stock[]) => {
    return stocks.reduce((acc: Record<string, Stock[]>, stock) => {
        const month = new Date(stock.purchaseDate).toLocaleString('en-US', { month: 'long', year: 'numeric' });
        if (!acc[month]) {
            acc[month] = [];
        }
        acc[month].push(stock);
        return acc;
    }, {});
};

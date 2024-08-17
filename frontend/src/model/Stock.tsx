export interface Stock {
    id: number;
    symbol: string;
    name: string;
    purchaseDate: string;
    purchasePrice: number;
    quantity: number;
    currentPrice?: number;
    profitOrLoss?: number;
    percentageChange?: number;
    profitOrLoss7Days?: number;
    percentageChange7Days?: number;
    profitOrLoss30Days?: number;
    percentageChange30Days?: number;
}
export interface Stock {
    id: number;
    symbol: string;
    purchaseDate: string;
    purchasePrice: number;
    quantity: number;
    currentPrice: number;
    profitOrLoss: number;
    percentageChange: number;
}
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/stocks';

interface Stock {
    id?: number;
    symbol: string;
    purchaseDate: string;
    purchasePrice: number;
    quantity: number;
    currentPrice?: number;
    profitOrLoss?: number;
}

class StockService {

    addStock(stock: Stock) {
        return axios.post(API_URL, stock);
    }

    getTransactions() {
        return axios.get<Stock[]>(`${API_URL}/transactions`);
    }

    deleteStock(id: number) {
        return axios.delete(`${API_URL}/${id}`);
    }

    getAggregatedReport() {
        return axios.get<Stock[]>(`${API_URL}/aggregated-report`);
    }
}

export default new StockService();

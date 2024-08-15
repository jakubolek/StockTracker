import axios from 'axios';
import {Stock} from "../model/Stock";
import {StockDto} from "../model/StockDto";
import {PortfolioSummaryData} from "../model/PortfolioSummaryData";

const API_URL = '/api/stocks';

class StockService {
    addStock(stock: StockDto) {
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

    getPortfolioSummary() {
        return axios.get<PortfolioSummaryData>(`${API_URL}/portfolio-summary`);
    }

}

export const stockService = new StockService();

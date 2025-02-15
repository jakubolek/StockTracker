import { useState } from 'react';
import { StockDto } from '../model/StockDto';

export const useStockForm = () => {
    const [formData, setFormData] = useState({
        symbol: '',
        name: '',
        purchaseDate: '',
        purchasePrice: 0,
        quantity: 0,
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'purchasePrice' || name === 'quantity'
                ? parseFloat(value.replace(',', '.').trim())
                : value,
        }));
    };

    const handleSubmit = (event: React.FormEvent, addStock: (stockDto: StockDto) => void) => {
        event.preventDefault();
        addStock(formData);
    };

    return {
        formData,
        setFormData,
        handleSubmit,
        handleInputChange,
    };
};

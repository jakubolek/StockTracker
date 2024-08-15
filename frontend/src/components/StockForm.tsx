import React, {useState} from 'react';
import {stockService} from '../services/StockService';
import '../css/StockForm.css';
import {StockDto} from "../model/StockDto";

const StockForm: React.FC = () => {
    const [symbol, setSymbol] = useState<string>('');
    const [name, setName] = useState<string>('');
    const [purchaseDate, setPurchaseDate] = useState<string>('');
    const [purchasePrice, setPurchasePrice] = useState<number>(0);
    const [quantity, setQuantity] = useState<number>(0);

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        const stockDto: StockDto = {symbol, name, purchaseDate, purchasePrice, quantity};
        stockService.addStock(stockDto)
            .then(() => {
                window.location.reload();
            })
            .catch((error) => {
                alert('There was an error adding the stock!\n' + (error.response?.data || 'Unknown error'));
                console.log(error.response?.data || error.message);
            });
    };

    return (
        <form className="stock-form" onSubmit={handleSubmit}>
            <h2>Add New Stock</h2>
            <div className="form-group">
                <label>Symbol:</label>
                <input type="text" value={symbol} onChange={(e) => setSymbol(e.target.value)} required/>
            </div>
            <div className="form-group">
                <label>Name:</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} required/>
            </div>
            <div className="form-group">
                <label>Purchase Date:</label>
                <input type="date" value={purchaseDate} onChange={(e) => setPurchaseDate(e.target.value)} required/>
            </div>
            <div className="form-group">
                <label>Purchase Price:</label>
                <input type="number" value={purchasePrice}
                       onChange={(e) => setPurchasePrice(parseFloat(e.target.value))} required/>
            </div>
            <div className="form-group">
                <label>Quantity:</label>
                <input type="number" value={quantity} onChange={(e) => setQuantity(parseFloat(e.target.value))}
                       required/>
            </div>
            <button className="submit-button" type="submit">Add Stock</button>
        </form>
    );
}

export default StockForm;

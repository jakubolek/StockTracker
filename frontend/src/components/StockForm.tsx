import React, {useState, useEffect} from 'react';
import {stockService} from '../services/StockService';
import '../css/StockForm.css';
import {StockDto} from "../model/StockDto";
import {StockSearchDto} from "../model/StockSearchDto";

const StockForm: React.FC = () => {
    const [symbol, setSymbol] = useState<string>('');
    const [name, setName] = useState<string>('');
    const [purchaseDate, setPurchaseDate] = useState<string>('');
    const [purchasePrice, setPurchasePrice] = useState<number>(0);
    const [quantity, setQuantity] = useState<number>(0);

    const [searchQuery, setSearchQuery] = useState<string>('');
    const [searchResults, setSearchResults] = useState<StockSearchDto[]>([]);

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

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        const parsedValue = parseFloat(value.replace(',', '.').trim());

        if (name === 'purchasePrice') {
            setPurchasePrice(parsedValue);
        } else if (name === 'quantity') {
            setQuantity(parsedValue);
        }
    };

    const handleSearch = async (query: string) => {
        if (query.length > 1) {
            try {
                const results = await stockService.searchStocks(query);
                setSearchResults(results.data);
            } catch (error) {
                console.error("Error fetching search results:", error);
            }
        } else {
            setSearchResults([]);
        }
    };

    useEffect(() => {
        handleSearch(searchQuery);
    }, [searchQuery]);

    return (
        <form className="stock-form" onSubmit={handleSubmit}>
            <h2>Add New Stock</h2>
            <div className="form-group">
                <label>Symbol:</label>
                <input type="text" value={symbol} onChange={(e) => {
                    const inputValue = e.target.value.trim();
                    setSymbol(inputValue);
                    setSearchQuery(inputValue);
                }}
                       required
                />
                {searchResults.length > 0 && (
                    <div className="autocomplete-results">
                        {searchResults.map((result) => (
                            <div
                                key={result.symbol}
                                className="autocomplete-item"
                                onClick={() => {
                                    setSymbol(result.symbol);
                                    setName(result.name);
                                    setSearchResults([]);
                                }}
                            >
                                {result.symbol} - {result.name}
                            </div>
                        ))}
                    </div>
                )}
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
                <input type="number" name="purchasePrice" value={purchasePrice} onChange={handleInputChange} required/>
            </div>
            <div className="form-group">
                <label>Quantity:</label>
                <input type="number" name="quantity" value={quantity} onChange={handleInputChange} required/>
            </div>
            <button className="submit-button" type="submit">Add Stock</button>
        </form>
    );
}

export default StockForm;

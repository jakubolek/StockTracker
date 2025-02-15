import React, { useState } from 'react';
import { useStockForm } from '../hooks/UseStockForm';
import { useStockSearch } from '../hooks/UseStockSearch';
import {stockService} from '../services/StockService';
import '../css/StockForm.css';
import {StockDto} from "../model/StockDto";
import {StockSearchDto} from "../model/StockSearchDto";

const StockForm: React.FC = () => {
    const { formData, handleSubmit, handleInputChange } = useStockForm();
    const { symbol, name, purchaseDate, purchasePrice, quantity } = formData;

    const [searchQuery, setSearchQuery] = useState<string>('');
    const [focusedField, setFocusedField] = useState<'symbol' | 'name' | null>(null);

    const { searchResults, isClickingAutocomplete, setIsClickingAutocomplete } = useStockSearch(searchQuery, focusedField);

    const addStock = (stockDto: StockDto) => {
        stockService.addStock(stockDto)
            .then(() => window.location.reload())
            .catch((error) => {
                alert('There was an error adding the stock!\n' + (error.response?.data || 'Unknown error'));
                console.error(error.response?.data || error.message);
            });
    };

    const handleAutocompleteSelect = (result: StockSearchDto) => {
        setSearchQuery('');

        handleInputChange({ target: { name: 'symbol', value: result.symbol } } as React.ChangeEvent<HTMLInputElement>);
        handleInputChange({ target: { name: 'name', value: result.name } } as React.ChangeEvent<HTMLInputElement>);
    };

    const renderAutocompleteResults = () => (
        searchResults.length > 0 && (
            <div
                className="autocomplete-results"
                onMouseDown={() => setIsClickingAutocomplete(true)}
                onMouseUp={() => setIsClickingAutocomplete(false)}
            >
                {searchResults.map((result) => (
                    <div
                        key={result.symbol}
                        className="autocomplete-item"
                        onClick={() => handleAutocompleteSelect(result)}
                    >
                        {result.symbol} - {result.name}
                    </div>
                ))}
            </div>
        )
    );

    const renderInputField = (label: string, name: string, value: string | number, onChange: (e: React.ChangeEvent<HTMLInputElement>) => void) => (
            <div className="form-group">
            <label>{label}:</label>
                <input
                type={name === 'purchaseDate' ? 'date' : 'text'}
                value={value}
                name={name}
                onFocus={() => setFocusedField(name as 'symbol' | 'name')}
                    onBlur={() => {
                    if (!isClickingAutocomplete) setFocusedField(null);
                    }}
                onChange={onChange}
                    required
                />
            {focusedField === name && renderAutocompleteResults()}
            </div>
    );

    return (
        <form className="stock-form" onSubmit={(e) => handleSubmit(e, addStock)}>
            <h2>Add New Stock</h2>
            {renderInputField('Symbol', 'symbol', symbol, (e) => {
                handleInputChange(e);
                setSearchQuery(e.target.value);
            })}
            {renderInputField('Name', 'name', name, (e) => {
                handleInputChange(e);
                setSearchQuery(e.target.value);
            })}
            {renderInputField('Purchase Date', 'purchaseDate', purchaseDate, handleInputChange)}
            {renderInputField('Purchase Price', 'purchasePrice', purchasePrice, handleInputChange)}
            {renderInputField('Quantity', 'quantity', quantity, handleInputChange)}

            <button className="submit-button" type="submit">Add Stock</button>
        </form>
    );
};

export default StockForm;

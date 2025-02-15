import { useState, useEffect } from 'react';
import { stockService } from '../services/StockService';
import { StockSearchDto } from "../model/StockSearchDto";

export const useStockSearch = (searchQuery: string, focusedField: 'symbol' | 'name' | null) => {
    const [searchResults, setSearchResults] = useState<StockSearchDto[]>([]);
    const [isClickingAutocomplete, setIsClickingAutocomplete] = useState<boolean>(false);

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
        if (focusedField) {
            handleSearch(searchQuery);
        }
    }, [searchQuery, focusedField]);

    return { searchResults, setSearchResults, isClickingAutocomplete, setIsClickingAutocomplete };
};

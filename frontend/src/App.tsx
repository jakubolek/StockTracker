import React from 'react';
import StockForm from './components/StockForm';
import StockTransactions from './components/StockTransactions';
import AggregatedReport from "./components/AggregatedReport";
import PortfolioSummary from "./components/PortfolioSummary";

const App: React.FC = () => {
    return (
        <div className="App">
            <StockForm/>
            <PortfolioSummary/>
            <StockTransactions/>
            <AggregatedReport/>
        </div>
    );
}

export default App;

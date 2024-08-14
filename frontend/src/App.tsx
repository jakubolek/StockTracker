import React from 'react';
import StockForm from './components/StockForm';
import StockTransactions from './components/StockTransactions';
import AggregatedReport from "./components/AggregatedReport";

const App: React.FC = () => {
    return (
        <div className="App">
            <StockForm/>
            <StockTransactions/>
            <AggregatedReport/>
        </div>
    );
}

export default App;

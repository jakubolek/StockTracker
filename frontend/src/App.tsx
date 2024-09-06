import React from 'react';
import StockForm from './components/StockForm';
import StockTransactions from './components/StockTransactions';
import AggregatedReport from "./components/AggregatedReport";
import PortfolioSummary from "./components/PortfolioSummary";
import "./App.css";
import {faGithub, faLinkedin} from '@fortawesome/free-brands-svg-icons';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const App: React.FC = () => {

    return (
        <div className="App">
            <div className="top-section">
                <StockForm/>
                <PortfolioSummary/>
            </div>
            <AggregatedReport/>
            <StockTransactions/>
            <footer className="footer">
                <div className="social-icons">
                    <a href="https://github.com/jakubolek" target="_blank" rel="noopener noreferrer">
                        <FontAwesomeIcon icon={faGithub} size="2x"/>
                    </a>
                    <a href="https://www.linkedin.com/in/jakub-olek" target="_blank" rel="noopener noreferrer">
                        <FontAwesomeIcon icon={faLinkedin} size="2x"/>
                    </a>
                </div>
                <p>© {new Date().getFullYear()} Jakub Olek. All rights reserved.</p>
            </footer>
        </div>
    );
}
export default App;

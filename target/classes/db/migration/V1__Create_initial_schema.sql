-- Create instruments table
CREATE TABLE instruments (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    exchange VARCHAR(100),
    sector VARCHAR(100),
    currency VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create portfolios table
CREATE TABLE portfolios (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create positions table
CREATE TABLE positions (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    instrument_id BIGINT NOT NULL REFERENCES instruments(id),
    quantity DECIMAL(19,4) NOT NULL,
    average_cost DECIMAL(19,4),
    market_value DECIMAL(19,4),
    weight DECIMAL(10,6),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(portfolio_id, instrument_id)
);

-- Create prices table
CREATE TABLE prices (
    id BIGSERIAL PRIMARY KEY,
    instrument_id BIGINT NOT NULL REFERENCES instruments(id),
    price_date DATE NOT NULL,
    open DECIMAL(19,4) NOT NULL,
    high DECIMAL(19,4) NOT NULL,
    low DECIMAL(19,4) NOT NULL,
    close DECIMAL(19,4) NOT NULL,
    volume BIGINT NOT NULL,
    adjusted_close DECIMAL(19,4),
    log_return DECIMAL(19,8),
    simple_return DECIMAL(19,8),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(instrument_id, price_date)
);

-- Create risk_runs table
CREATE TABLE risk_runs (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL REFERENCES portfolios(id),
    run_date DATE NOT NULL,
    var_method VARCHAR(20) NOT NULL,
    confidence_level DECIMAL(5,4) NOT NULL,
    window_size INTEGER NOT NULL,
    var_95 DECIMAL(19,4),
    var_99 DECIMAL(19,4),
    expected_shortfall_95 DECIMAL(19,4),
    expected_shortfall_99 DECIMAL(19,4),
    portfolio_value DECIMAL(19,4) NOT NULL,
    portfolio_volatility DECIMAL(10,6),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    error_message TEXT,
    execution_time_ms BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create risk_breakdowns table
CREATE TABLE risk_breakdowns (
    id BIGSERIAL PRIMARY KEY,
    risk_run_id BIGINT NOT NULL REFERENCES risk_runs(id),
    instrument_id BIGINT NOT NULL REFERENCES instruments(id),
    position_value DECIMAL(19,4) NOT NULL,
    weight DECIMAL(10,6) NOT NULL,
    marginal_var DECIMAL(19,4),
    component_var DECIMAL(19,4),
    individual_var DECIMAL(19,4),
    volatility DECIMAL(10,6),
    beta DECIMAL(10,6),
    correlation DECIMAL(10,6),
    contribution_percentage DECIMAL(5,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(risk_run_id, instrument_id)
);

-- Create indexes for performance
CREATE INDEX idx_instruments_symbol ON instruments(symbol);
CREATE INDEX idx_instruments_type ON instruments(type);
CREATE INDEX idx_portfolios_status ON portfolios(status);
CREATE INDEX idx_positions_portfolio_id ON positions(portfolio_id);
CREATE INDEX idx_positions_instrument_id ON positions(instrument_id);
CREATE INDEX idx_prices_instrument_id ON prices(instrument_id);
CREATE INDEX idx_prices_date ON prices(price_date);
CREATE INDEX idx_prices_instrument_date ON prices(instrument_id, price_date);
CREATE INDEX idx_risk_runs_portfolio_id ON risk_runs(portfolio_id);
CREATE INDEX idx_risk_runs_date ON risk_runs(run_date);
CREATE INDEX idx_risk_runs_status ON risk_runs(status);
CREATE INDEX idx_risk_breakdowns_risk_run_id ON risk_breakdowns(risk_run_id);
CREATE INDEX idx_risk_breakdowns_instrument_id ON risk_breakdowns(instrument_id);

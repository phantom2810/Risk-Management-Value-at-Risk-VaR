#!/bin/bash

# Demo script for VaR Calculator
# This script demonstrates the end-to-end workflow

echo "=== VaR Calculator Demo ==="
echo

# Check if the application is running
echo "Checking if application is running..."
curl -s http://localhost:8080/api/actuator/health > /dev/null
if [ $? -eq 0 ]; then
    echo "✅ Application is running"
else
    echo "❌ Application is not running. Please start the application first."
    echo "Run: mvn spring-boot:run"
    exit 1
fi

echo

# 1. Create a sample portfolio
echo "1. Creating sample portfolio..."
PORTFOLIO_RESPONSE=$(curl -s -X POST http://localhost:8080/api/portfolio \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Demo Portfolio",
    "description": "Sample portfolio for VaR calculation demo",
    "baseCurrency": "USD"
  }')

if [ $? -eq 0 ]; then
    PORTFOLIO_ID=$(echo $PORTFOLIO_RESPONSE | grep -o '"id":[0-9]*' | cut -d: -f2)
    echo "✅ Portfolio created with ID: $PORTFOLIO_ID"
else
    echo "❌ Failed to create portfolio"
    exit 1
fi

echo

# 2. Upload positions (would need actual CSV upload endpoint)
echo "2. Sample portfolio positions available in: sample-data/sample-portfolio.csv"
echo "   Contains positions for: AAPL, GOOGL, MSFT, TSLA, AMZN, META, NVDA, SPY, QQQ, IWM"

echo

# 3. Calculate VaR
echo "3. Calculating Historical VaR..."
VAR_RESPONSE=$(curl -s -X POST http://localhost:8080/api/portfolio/$PORTFOLIO_ID/risk/run \
  -H "Content-Type: application/json" \
  -d '{
    "varMethod": "HISTORICAL",
    "confidenceLevels": [0.95, 0.99],
    "windowSize": 252
  }')

if [ $? -eq 0 ]; then
    echo "✅ VaR calculation initiated"
    echo "Response: $VAR_RESPONSE"
else
    echo "❌ Failed to calculate VaR"
fi

echo

# 4. Get portfolio details
echo "4. Getting portfolio details..."
curl -s http://localhost:8080/api/portfolio/$PORTFOLIO_ID | json_pp 2>/dev/null || echo "Portfolio details retrieved"

echo
echo "=== Demo completed ==="
echo
echo "Next steps:"
echo "- Access the frontend dashboard at: http://localhost:3000"
echo "- View API documentation at: http://localhost:8080/swagger-ui.html"
echo "- Check application health at: http://localhost:8080/api/actuator/health"

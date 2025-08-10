# Risk Management & Value-at-Risk (VaR) Calculator

A comprehensive risk management system that calculates Value-at-Risk (VaR) for investment portfolios using multiple methodologies including Historical VaR, Parametric VaR, and Monte Carlo VaR.

## 🚀 Features

### Core Functionality

- **Portfolio Management**: Create and manage investment portfolios
- **Position Management**: Upload portfolio positions via CSV
- **Price Data Integration**: Fetch historical price data from external APIs
- **Multiple VaR Methods**:
  - Historical VaR (quantile-based)
  - Parametric VaR (variance-covariance method)
  - Monte Carlo VaR (simulation-based)
- **Risk Analytics**: Detailed risk breakdown and contribution analysis
- **Report Generation**: Export results to PDF and Excel formats
- **Scheduled Calculations**: Automated daily VaR calculations
- **REST API**: Complete RESTful API for all operations

### Frontend Dashboard

- Interactive portfolio upload and management
- Real-time price history visualization
- VaR calculation with configurable parameters
- Risk breakdown charts and tables
- Report download functionality

## 🛠 Technology Stack

### Backend

- **Java 17** - Programming language
- **Spring Boot 3.2** - Application framework
- **PostgreSQL** - Database
- **Flyway** - Database migration
- **Lombok** - Code generation
- **Apache POI** - Excel generation
- **iText** - PDF generation
- **Quartz** - Job scheduling
- **Apache Commons Math** - Mathematical computations

### Frontend

- **React** with TypeScript
- **Material-UI** - UI components
- **Charts.js** - Data visualization

### Testing

- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Testcontainers** - Integration testing

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Node.js 16+ (for frontend)
- Alpha Vantage API key (for price data)

## 🚀 Quick Start

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE var_calculator;

-- Create user
CREATE USER var_user WITH PASSWORD 'var_password';
GRANT ALL PRIVILEGES ON DATABASE var_calculator TO var_user;
```

### 2. Backend Setup

```bash
# Clone the repository
git clone <repository-url>
cd var-calculator

# Set environment variables
export ALPHA_VANTAGE_API_KEY=your_api_key_here

# Run the application
mvn spring-boot:run
```

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

### 4. Access the Application

- **Backend API**: http://localhost:8080/api
- **Frontend Dashboard**: http://localhost:3000
- **API Documentation**: http://localhost:8080/api/swagger-ui.html

## 📖 API Endpoints

### Portfolio Management

- `POST /api/portfolio` - Create a new portfolio
- `GET /api/portfolio/{id}` - Get portfolio details
- `PUT /api/portfolio/{id}` - Update portfolio
- `DELETE /api/portfolio/{id}` - Delete portfolio

### Position Management

- `POST /api/portfolio/{id}/positions/upload` - Upload positions via CSV
- `GET /api/portfolio/{id}/positions` - Get portfolio positions

### Price Data

- `POST /api/portfolio/{id}/prices/load` - Load price history
- `GET /api/instrument/{id}/prices` - Get price history

### Risk Calculations

- `POST /api/portfolio/{id}/risk/run` - Run VaR calculation
- `GET /api/risk/{runId}` - Get VaR results
- `GET /api/risk/{runId}/report` - Download report

## 📊 VaR Calculation Methods

### 1. Historical VaR

- Uses historical return distribution
- Calculates quantiles based on confidence level
- No distributional assumptions

### 2. Parametric VaR

- Assumes normal distribution of returns
- Uses variance-covariance matrix
- Faster computation for large portfolios

### 3. Monte Carlo VaR

- Simulates portfolio returns
- Can incorporate fat tails and skewness
- Most flexible but computationally intensive

## 📈 Sample Portfolio CSV Format

```csv
symbol,quantity,averageCost
AAPL,100,150.00
GOOGL,50,2500.00
MSFT,75,300.00
TSLA,25,800.00
```

## ⚙️ Configuration

Key configuration options in `application.yml`:

```yaml
var-calculator:
  var:
    default-confidence-levels: [0.95, 0.99]
    default-window-size: 252
    monte-carlo-simulations: 10000
  price-data:
    alpha-vantage:
      api-key: ${ALPHA_VANTAGE_API_KEY}
  scheduler:
    daily-var-calculation:
      enabled: true
      cron: "0 0 6 * * MON-FRI"
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run integration tests
mvn test -Dtest=*IT

# Run with coverage
mvn clean test jacoco:report
```

## 📦 Build and Deployment

```bash
# Build the application
mvn clean package

# Run the JAR file
java -jar target/var-calculator-1.0.0.jar

# Build Docker image
docker build -t var-calculator .

# Run with Docker Compose
docker-compose up -d
```

## 📚 Project Structure

```
src/
├── main/
│   ├── java/com/riskmanagement/varcalculator/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── entity/         # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/
│       ├── application.yml # Configuration
│       └── db/migration/   # Flyway migrations
└── test/                   # Test classes
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:

- Create an issue in the GitHub repository
- Check the documentation in the `/docs` folder
- Review the API documentation at `/swagger-ui.html`

## 🔮 Roadmap

### Phase 1 (Current)

- ✅ Basic portfolio and position management
- ✅ Historical and Parametric VaR
- ✅ REST API implementation
- ✅ Database schema and migrations

### Phase 2 (Next)

- 🔄 Monte Carlo VaR implementation
- 🔄 Frontend dashboard
- 🔄 Report generation
- 🔄 Scheduled calculations

### Phase 3 (Future)

- ⏳ Advanced risk metrics (Expected Shortfall)
- ⏳ Factor models integration
- ⏳ Real-time calculations
- ⏳ Kafka/Spark integration

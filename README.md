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

- **Java 16** - Programming language
- **Spring Boot 2.7.18** - Application framework
- **PostgreSQL 15** - Database
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

- Java 16 or higher
- Maven 3.6+
- PostgreSQL 12+ (tested with PostgreSQL 15)
- Node.js 16+ (for frontend)
- Alpha Vantage API key (for price data, optional for basic testing)

## 🚀 Quick Start

### 1. Database Setup (PostgreSQL)

**Option A: Using Homebrew (macOS)**

```bash
# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Create database and user
createuser var_user
createdb -O var_user var_calculator

# Set password for user
psql -d var_calculator -c "ALTER USER var_user WITH PASSWORD 'var_password';"
```

**Option B: Manual SQL Setup**

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
cd Risk-Management-Value-at-Risk-VaR

# Set environment variables (optional for basic testing)
export ALPHA_VANTAGE_API_KEY=your_api_key_here

# Build and run the application
mvn clean package
java -jar target/var-calculator-1.0.0.jar

# OR run directly with Maven
mvn spring-boot:run
```

### 3. Verify Installation

```bash
# Check application health
curl http://localhost:8080/api/actuator/health

# Test API endpoints
curl http://localhost:8080/api/api/portfolio

# Create a test portfolio
curl -X POST "http://localhost:8080/api/api/portfolio" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Portfolio", "description": "Sample portfolio", "baseCurrency": "USD"}'
```

### 4. Frontend Setup (Optional)

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

### 5. Access the Application

- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/actuator/health
- **Frontend Dashboard**: http://localhost:3000 (if frontend is set up)
- **API Documentation**: http://localhost:8080/api/swagger-ui.html (if Swagger is configured)

## ⚡ Quick Start Example

Once the application is running, try these commands to test the functionality:

```bash
# 1. Check application health
curl http://localhost:8080/api/actuator/health

# 2. Create a portfolio
curl -X POST "http://localhost:8080/api/api/portfolio" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Investment Portfolio",
    "description": "Diversified equity portfolio",
    "baseCurrency": "USD"
  }'

# 3. List all portfolios
curl http://localhost:8080/api/api/portfolio

# 4. Get specific portfolio (replace {id} with actual ID from step 2)
curl http://localhost:8080/api/api/portfolio/1

# Expected response format:
# {
#   "id": 1,
#   "name": "My Investment Portfolio",
#   "description": "Diversified equity portfolio",
#   "baseCurrency": "USD",
#   "status": "ACTIVE",
#   "createdAt": "2025-08-10T12:59:07.020184",
#   "positionCount": 0,
#   "totalMarketValue": null
# }
```

## 📖 API Endpoints

### Portfolio Management

- `POST /api/api/portfolio` - Create a new portfolio
- `GET /api/api/portfolio` - Get all portfolios
- `GET /api/api/portfolio/{id}` - Get portfolio details
- `PUT /api/api/portfolio/{id}` - Update portfolio
- `DELETE /api/api/portfolio/{id}` - Delete portfolio

### Position Management

- `POST /api/api/portfolio/{id}/positions/upload` - Upload positions via CSV
- `GET /api/api/portfolio/{id}/positions` - Get portfolio positions

### Price Data

- `POST /api/api/portfolio/{id}/prices/load` - Load price history
- `GET /api/api/instrument/{id}/prices` - Get price history

### Risk Calculations

- `POST /api/api/portfolio/{id}/risk/run` - Run VaR calculation
- `GET /api/api/risk/{runId}` - Get VaR results
- `GET /api/api/risk/{runId}/report` - Download report

### Health & Monitoring

- `GET /api/actuator/health` - Application health check
- `GET /api/actuator/info` - Application information
- `GET /api/actuator/metrics` - Application metrics

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
# Server Configuration
server:
  port: 8080
  servlet:
    context-path: /api

# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/var_calculator
    username: var_user
    password: var_password
    driver-class-name: org.postgresql.Driver

  # JPA Configuration
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false

  # Flyway Migration
  flyway:
    enabled: true
    locations: classpath:db/migration

# Application Specific Configuration
var-calculator:
  var:
    default-confidence-levels: [0.95, 0.99]
    default-window-size: 252 # Trading days in a year
    monte-carlo-simulations: 10000
  price-data:
    alpha-vantage:
      api-key: ${ALPHA_VANTAGE_API_KEY:demo}
  scheduler:
    daily-var-calculation:
      enabled: true
      cron: "0 0 6 * * MON-FRI" # 6 AM on weekdays

# Management/Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### Environment Variables

- `ALPHA_VANTAGE_API_KEY`: API key for price data (optional for basic testing)
- `JAVA_HOME`: Path to Java 16+ installation

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run tests with output
mvn test 2>&1

# Clean build and test
mvn clean test

# Package without running tests
mvn clean package -DskipTests

# Verify installation with integration test
curl -X POST "http://localhost:8080/api/api/portfolio" \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Portfolio", "description": "Integration test", "baseCurrency": "USD"}'
```

## 📦 Build and Deployment

```bash
# Build the application
mvn clean package

# Run the JAR file
java -jar target/var-calculator-1.0.0.jar

# Build with specific Java version
export JAVA_HOME=/path/to/java16
mvn clean package

# Check if application is running
curl http://localhost:8080/api/actuator/health

# Stop the application (if running in background)
# Find the process: ps aux | grep var-calculator
# Kill the process: kill <PID>
```

### Docker Deployment (Future)

```bash
# Build Docker image (when Dockerfile is available)
docker build -t var-calculator .

# Run with Docker Compose (when docker-compose.yml is updated)
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

## 🔧 Troubleshooting

### Common Issues

#### 1. Java Version Compatibility

```bash
# Check Java version
java -version

# Should show Java 16 or higher
# If using Java 21+, it should still work but may have warnings
```

#### 2. Database Connection Issues

```bash
# Check if PostgreSQL is running
brew services list | grep postgresql

# Start PostgreSQL if not running
brew services start postgresql@15

# Test database connection
psql -d var_calculator -U var_user -h localhost
```

#### 3. Application Startup Errors

**Issue: HQL Query Syntax Error**

- **Solution**: Ensure the `PriceRepository.findTopNByInstrumentId` method uses native SQL query

**Issue: Port Already in Use**

```bash
# Find what's using port 8080
lsof -i :8080

# Kill the process if needed
kill -9 <PID>
```

#### 4. API Endpoint 404 Errors

- **Correct Base URL**: `http://localhost:8080/api/api/portfolio`
- **Note**: The context path `/api` is configured in `application.yml`

#### 5. Maven Build Issues

```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Reinstall dependencies
mvn clean install

# Force update dependencies
mvn clean install -U
```

### Verification Steps

1. **Database Connection**: Check health endpoint shows database as UP
2. **API Functionality**: Create and retrieve a test portfolio
3. **Unit Tests**: All 5 tests in PortfolioServiceTest should pass
4. **Application Logs**: No ERROR level messages during startup

## 🆘 Support

For support and questions:

- Create an issue in the GitHub repository
- Check the documentation in the `/docs` folder
- Review the API documentation at `/swagger-ui.html`

## 🔮 Roadmap

### Phase 1 (Completed ✅)

- ✅ Basic portfolio and position management
- ✅ Database schema and migrations (PostgreSQL + Flyway)
- ✅ REST API implementation with full CRUD operations
- ✅ Unit testing with Mockito and JUnit 5
- ✅ Application health monitoring with Actuator
- ✅ Historical and Parametric VaR foundations

### Phase 2 (In Progress 🔄)

- 🔄 Monte Carlo VaR implementation
- 🔄 Frontend dashboard completion
- 🔄 CSV position upload functionality
- 🔄 Price data integration with external APIs
- 🔄 Report generation (PDF/Excel)

### Phase 3 (Planned ⏳)

- ⏳ Scheduled VaR calculations with Quartz
- ⏳ Advanced risk metrics (Expected Shortfall, Conditional VaR)
- ⏳ Real-time price data integration
- ⏳ Factor models and correlation analysis
- ⏳ Performance optimization and caching

### Phase 4 (Future 🔮)

- 🔮 Kafka/Spark integration for big data
- 🔮 Machine learning risk models
- 🔮 Multi-tenancy support
- 🔮 Advanced visualization and dashboards

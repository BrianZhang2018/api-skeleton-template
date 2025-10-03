# API Skeleton Template

A production-ready microservice template built with Java 17, Jersey (JAX-RS), Google Guice, and JDBI.

## Features

- **3-Layer Architecture**: Clean separation of Resources → Services → DAO
- **Dependency Injection**: Google Guice with explicit bindings
- **RESTful API**: Jersey 3.1.3 (JAX-RS 3.0)
- **Database Access**: JDBI 3.45.1 with PostgreSQL
- **Connection Pooling**: HikariCP for high performance
- **JWT Authentication**: Stateless auth with Auth0 JWT
- **Logging**: SLF4J with Logback (console + rolling file)
- **Environment Configuration**: 12-factor app principles
- **Health Checks**: Built-in health endpoint

## Technology Stack

- Java 17
- Maven 3.x
- Jersey 3.1.3 (JAX-RS 3.0)
- Google Guice 7.0.0
- JDBI 3.45.1
- PostgreSQL 15
- HikariCP 5.0.1
- Auth0 JWT 4.4.0
- Jackson 2.15.2
- Logback 1.4.11

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.x
- Docker & Docker Compose

### 1. Start Database

```bash
docker-compose up -d
```

### 2. Configure Environment

```bash
cp .env.example .env
# Edit .env with your configuration
```

### 3. Build and Run

```bash
mvn clean compile exec:java
```

The API will start on `http://localhost:8080`

### 4. Test Endpoints

```bash
# Health check
curl http://localhost:8080/api/v1/health

# Test with demo token
export TOKEN="demo-token"
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/users

# Run comprehensive tests
./test-endpoints.sh
```

## API Endpoints

| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| GET | `/api/v1/health` | No | Health check |
| GET | `/api/v1/users` | Yes | List all users |
| POST | `/api/v1/users` | Yes | Create a user |
| GET | `/api/v1/users/{id}` | Yes | Get user by ID |
| DELETE | `/api/v1/users/{id}` | Yes | Delete user |

## Authentication

All endpoints except `/health` require JWT authentication.

### Using Demo Token

For testing, use the demo token:

```bash
curl -H "Authorization: Bearer demo-token" http://localhost:8080/api/v1/users
```

### Generating Real JWT Token

```bash
# Generate token with default user
./generate-test-jwt.sh

# Generate token with custom user
./generate-test-jwt.sh user456 custom@example.com
```

## Architecture

### Dependency Injection Flow

```
Main.java
  └─> Creates Guice Injector
       └─> Loads MainModule
            ├─> Installs GreetingModule
            ├─> Installs JwtModule
            ├─> Binds Resources (via jaxrsBinder)
            ├─> Binds Services
            ├─> Binds DAOs
            └─> Provides DataSource & Jdbi
```

### Request Flow

```
HTTP Request
  └─> Jersey/Grizzly Server
       └─> JwtAuthFilter (authentication)
            └─> UserResource (JAX-RS endpoint)
                 └─> UserService (business logic)
                      └─> UserDao (database access)
                           └─> PostgreSQL
```

## Project Structure

```
api-skeleton-template/
├── pom.xml                              # Maven configuration
├── docker-compose.yml                   # PostgreSQL container
├── .env.example                         # Environment template
├── src/main/java/com/example/api/
│   ├── Main.java                        # Application entry point
│   ├── MainModule.java                  # Main Guice module
│   ├── JerseyConfig.java                # Jersey configuration
│   ├── GreetingModule.java              # Example Guice module
│   ├── auth/
│   │   ├── JwtModule.java               # JWT Guice module
│   │   ├── JwtAuthFilter.java           # JWT authentication filter
│   │   ├── JwtConfig.java               # JWT configuration
│   │   └── JwtRequired.java             # JWT annotation
│   ├── config/
│   │   ├── DatabaseConfig.java          # Database configuration
│   │   └── JwtConfig.java               # JWT configuration
│   ├── data/
│   │   ├── User.java                    # User entity with Builder
│   │   └── ApiResponse.java             # API response wrapper
│   ├── dao/
│   │   └── UserDao.java                 # User database operations
│   ├── resources/
│   │   ├── HealthResource.java          # Health check endpoint
│   │   └── UserResource.java            # User CRUD endpoints
│   ├── services/
│   │   ├── GreetingService.java         # Greeting service interface
│   │   ├── GreetingServiceImpl.java     # Greeting implementation
│   │   └── UserService.java             # User business logic
│   ├── jaxrs/
│   │   ├── JaxrsBinder.java             # Resource binding utility
│   │   └── JaxrsResource.java           # Resource annotation
│   ├── exceptions/
│   │   └── ValidationException.java     # Validation exception
│   └── util/
│       └── JwtGenerator.java            # JWT token generator
└── src/main/resources/
    └── logback.xml                      # Logging configuration
```

## Development

### Running Tests

```bash
mvn test
```

### Building JAR

```bash
mvn clean package
java -jar target/api-skeleton-template-1.0.0.jar
```

### Viewing Logs

```bash
# Console output
mvn exec:java

# File logs
tail -f logs/api-skeleton-template.log
```

## Configuration

Environment variables (see `.env.example`):

| Variable | Default | Description |
|----------|---------|-------------|
| DATABASE_URL | jdbc:postgresql://localhost:5433/api_skeleton | Database connection URL |
| DATABASE_USERNAME | postgres | Database username |
| DATABASE_PASSWORD | password | Database password |
| DB_MAX_POOL_SIZE | 10 | Maximum connection pool size |
| DB_MIN_POOL_SIZE | 2 | Minimum idle connections |
| JWT_SECRET | your-secret-key-change-in-production | JWT signing secret |
| JWT_ISSUER | api-skeleton-template | JWT issuer |
| JWT_EXPIRATION | 86400 | Token expiration (seconds) |
| SERVER_PORT | 8080 | HTTP server port |

## License

MIT

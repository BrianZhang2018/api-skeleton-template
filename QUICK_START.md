# Quick Start Guide

Get the API Skeleton Template running in 5 minutes.

## Prerequisites

- Java 17+
- Maven 3.x
- Docker Desktop

## Step 1: Clone and Navigate

```bash
cd api-skeleton-template
```

## Step 2: Start Database

```bash
docker-compose up -d
```

Verify it's running:

```bash
docker-compose ps
```

## Step 3: Run the Application

```bash
mvn clean compile exec:java
```

You should see:

```
===================================================
API Skeleton Template started successfully!
Base URL: http://0.0.0.0:8080/
===================================================
```

## Step 4: Test the API

Open a new terminal and run:

```bash
# Test health endpoint
curl http://localhost:8080/api/v1/health

# Test protected endpoint with demo token
curl -H "Authorization: Bearer demo-token" \
  http://localhost:8080/api/v1/users
```

## Step 5: Create a User

```bash
curl -X POST \
  -H "Authorization: Bearer demo-token" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}' \
  http://localhost:8080/api/v1/users
```

## Step 6: List Users

```bash
curl -H "Authorization: Bearer demo-token" \
  http://localhost:8080/api/v1/users
```

## That's It!

You now have a fully functional microservice running with:
- ✅ REST API
- ✅ Database persistence
- ✅ JWT authentication
- ✅ Dependency injection
- ✅ Logging

## Next Steps

- Run comprehensive tests: `./test-endpoints.sh`
- Generate real JWT tokens: `./generate-test-jwt.sh`
- Check logs: `tail -f logs/api-skeleton-template.log`
- Read the full [README.md](README.md) for architecture details

## Troubleshooting

### Port 8080 already in use

```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Database connection error

```bash
# Check database is running
docker-compose ps

# Restart database
docker-compose restart postgres
```

### Build errors

```bash
# Clean and rebuild
mvn clean install
```

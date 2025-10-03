#!/bin/bash

# Simple API Test Script
# Basic health check and authentication test

BASE_URL="http://localhost:8080"

echo "Testing API Skeleton Template..."
echo ""

# Test health endpoint
echo "1. Testing Health Endpoint:"
curl -s "$BASE_URL/api/v1/health"
echo ""
echo ""

# Test protected endpoint without auth (should fail)
echo "2. Testing Protected Endpoint (no auth - should fail):"
curl -s "$BASE_URL/api/v1/users"
echo ""
echo ""

# Test protected endpoint with demo token (should succeed)
echo "3. Testing Protected Endpoint (with demo-token - should succeed):"
curl -s -H "Authorization: Bearer demo-token" "$BASE_URL/api/v1/users"
echo ""
echo ""

echo "Tests completed!"

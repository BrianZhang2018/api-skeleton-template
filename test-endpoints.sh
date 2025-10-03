#!/bin/bash

# Comprehensive API Test Suite
# Tests all endpoints with proper formatting

BASE_URL="http://localhost:8080"
TOKEN="demo-token"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=============================================="
echo "API Skeleton Template - Test Suite"
echo "=============================================="
echo ""

# Test 1: Health Check
echo -e "${YELLOW}Test 1: Health Check${NC}"
echo "GET $BASE_URL/api/v1/health"
curl -s "$BASE_URL/api/v1/health" | jq '.'
echo ""

# Test 2: Get Users (with auth)
echo -e "${YELLOW}Test 2: Get All Users (with JWT)${NC}"
echo "GET $BASE_URL/api/v1/users"
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 3: Create User (with auth)
echo -e "${YELLOW}Test 3: Create User (with JWT)${NC}"
echo "POST $BASE_URL/api/v1/users"
curl -s -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}' \
  "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 4: Create Another User
echo -e "${YELLOW}Test 4: Create Another User${NC}"
echo "POST $BASE_URL/api/v1/users"
curl -s -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Smith","email":"jane@example.com"}' \
  "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 5: Get All Users (should show created users)
echo -e "${YELLOW}Test 5: Get All Users (after creation)${NC}"
echo "GET $BASE_URL/api/v1/users"
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 6: Get Specific User
echo -e "${YELLOW}Test 6: Get User by ID${NC}"
echo "GET $BASE_URL/api/v1/users/1"
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/v1/users/1" | jq '.'
echo ""

# Test 7: Unauthorized Access (no token)
echo -e "${YELLOW}Test 7: Unauthorized Access (no token)${NC}"
echo "GET $BASE_URL/api/v1/users (no auth header)"
curl -s "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 8: Validation Error
echo -e "${YELLOW}Test 8: Validation Error (empty name)${NC}"
echo "POST $BASE_URL/api/v1/users"
curl -s -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"invalid@example.com"}' \
  "$BASE_URL/api/v1/users" | jq '.'
echo ""

# Test 9: Invalid Email
echo -e "${YELLOW}Test 9: Validation Error (invalid email)${NC}"
echo "POST $BASE_URL/api/v1/users"
curl -s -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"notanemail"}' \
  "$BASE_URL/api/v1/users" | jq '.'
echo ""

echo "=============================================="
echo "All tests completed!"
echo "=============================================="

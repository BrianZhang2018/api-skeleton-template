#!/bin/bash

# JWT Generator Script
# Generates a valid JWT token for testing

# Configuration
SECRET="${JWT_SECRET:-your-secret-key-change-in-production}"
ISSUER="${JWT_ISSUER:-api-skeleton-template}"
USER_ID="${1:-user123}"
EMAIL="${2:-user@example.com}"
EXPIRATION="${JWT_EXPIRATION:-86400}" # 24 hours in seconds

# Calculate expiration timestamp
EXP=$(($(date +%s) + EXPIRATION))
IAT=$(date +%s)

# Create header (base64url encoded)
HEADER='{"alg":"HS256","typ":"JWT"}'
HEADER_B64=$(echo -n "$HEADER" | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')

# Create payload (base64url encoded)
PAYLOAD="{\"iss\":\"$ISSUER\",\"user_id\":\"$USER_ID\",\"email\":\"$EMAIL\",\"iat\":$IAT,\"exp\":$EXP}"
PAYLOAD_B64=$(echo -n "$PAYLOAD" | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')

# Create signature
SIGNATURE=$(echo -n "${HEADER_B64}.${PAYLOAD_B64}" | openssl dgst -sha256 -hmac "$SECRET" -binary | openssl base64 -e | tr -d '=' | tr '/+' '_-' | tr -d '\n')

# Combine to form JWT
JWT="${HEADER_B64}.${PAYLOAD_B64}.${SIGNATURE}"

echo "Generated JWT Token:"
echo "$JWT"
echo ""
echo "Token Details:"
echo "  User ID: $USER_ID"
echo "  Email: $EMAIL"
echo "  Issued At: $(date -r $IAT)"
echo "  Expires At: $(date -r $EXP)"
echo ""
echo "Usage:"
echo "  export TOKEN=\"$JWT\""
echo "  curl -H \"Authorization: Bearer \$TOKEN\" http://localhost:8080/api/v1/users"

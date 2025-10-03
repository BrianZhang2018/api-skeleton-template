package com.example.api.config;

import jakarta.inject.Singleton;

@Singleton
public class JwtConfig {
    private final String secret = System.getenv().getOrDefault("JWT_SECRET",
        "your-secret-key-change-in-production");
    private final String issuer = System.getenv().getOrDefault("JWT_ISSUER",
        "api-skeleton-template");
    private final long expiration = Long.parseLong(System.getenv().getOrDefault("JWT_EXPIRATION", "86400"));

    public String getSecret() {
        return secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public long getExpiration() {
        return expiration;
    }
}

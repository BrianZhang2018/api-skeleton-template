package com.example.api.config;

import jakarta.inject.Singleton;

@Singleton
public class DatabaseConfig {
    private final String url = System.getenv().getOrDefault("DATABASE_URL",
            "jdbc:postgresql://localhost:5432/api_skeleton");
    private final String username = System.getenv().getOrDefault("DATABASE_USERNAME", "postgres");
    private final String password = System.getenv().getOrDefault("DATABASE_PASSWORD", "password");
    private final int maxPoolSize = Integer.parseInt(System.getenv().getOrDefault("DB_MAX_POOL_SIZE", "10"));
    private final int minPoolSize = Integer.parseInt(System.getenv().getOrDefault("DB_MIN_POOL_SIZE", "2"));
    private final long connectionTimeout = Long
            .parseLong(System.getenv().getOrDefault("DB_CONNECTION_TIMEOUT", "30000"));
    private final long idleTimeout = Long.parseLong(System.getenv().getOrDefault("DB_IDLE_TIMEOUT", "600000"));
    private final long maxLifetime = Long.parseLong(System.getenv().getOrDefault("DB_MAX_LIFETIME", "1800000"));

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public long getMaxLifetime() {
        return maxLifetime;
    }
}

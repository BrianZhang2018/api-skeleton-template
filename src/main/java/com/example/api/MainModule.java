package com.example.api;

import com.example.api.auth.JwtModule;
import com.example.api.config.DatabaseConfig;
import com.example.api.dao.UserDao;
import com.example.api.greeting.GreetingModule;
import com.example.api.resources.HealthResource;
import com.example.api.resources.UserResource;
import com.example.api.services.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;

import javax.sql.DataSource;

import static com.example.api.jaxrs.JaxrsBinder.jaxrsBinder;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        // Install sub-modules
        install(new GreetingModule());
        install(new JwtModule());

        // Bind configuration
        bind(DatabaseConfig.class);

        // Bind services
        bind(UserService.class);

        // Bind DAOs
        bind(UserDao.class);

        // Register JAX-RS resources using jaxrsBinder pattern
        jaxrsBinder(binder()).bind(HealthResource.class);
        jaxrsBinder(binder()).bind(UserResource.class);
    }

    @Provides
    @Singleton
    public DataSource provideDataSource(DatabaseConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        hikariConfig.setMinimumIdle(config.getMinPoolSize());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setMaxLifetime(config.getMaxLifetime());

        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new PostgresPlugin());
        return jdbi;
    }
}

package com.example.api.dao;

import com.example.api.data.User;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private final Jdbi jdbi;

    @Inject
    public UserDao(Jdbi jdbi) {
        this.jdbi = jdbi;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        jdbi.useHandle(handle -> {
            handle.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  id SERIAL PRIMARY KEY," +
                "  name VARCHAR(255) NOT NULL," +
                "  email VARCHAR(255) NOT NULL UNIQUE," +
                "  created_at TIMESTAMP NOT NULL DEFAULT NOW()" +
                ")"
            );
            logger.info("Users table created or already exists");
        });
    }

    public User insert(User user) {
        return jdbi.withHandle(handle -> {
            Long id = handle.createUpdate(
                "INSERT INTO users (name, email, created_at) VALUES (:name, :email, :createdAt)"
            )
            .bind("name", user.getName())
            .bind("email", user.getEmail())
            .bind("createdAt", user.getCreatedAt())
            .executeAndReturnGeneratedKeys("id")
            .mapTo(Long.class)
            .one();

            logger.info("Inserted user with id: {}", id);

            return User.builder()
                .id(id)
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
        });
    }

    public Optional<User> findById(Long id) {
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM users WHERE id = :id")
                .bind("id", id)
                .map((rs, ctx) -> User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .createdAt(rs.getTimestamp("created_at").toInstant())
                    .build())
                .findFirst()
        );
    }

    public List<User> findAll() {
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT * FROM users ORDER BY created_at DESC")
                .map((rs, ctx) -> User.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .createdAt(rs.getTimestamp("created_at").toInstant())
                    .build())
                .list()
        );
    }

    public boolean deleteById(Long id) {
        return jdbi.withHandle(handle -> {
            int rowsDeleted = handle.createUpdate("DELETE FROM users WHERE id = :id")
                .bind("id", id)
                .execute();
            return rowsDeleted > 0;
        });
    }
}

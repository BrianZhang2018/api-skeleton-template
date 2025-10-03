package com.example.api.services;

import com.example.api.dao.UserDao;
import com.example.api.data.User;
import com.example.api.exceptions.ValidationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email) {
        logger.info("Creating user with name: {}, email: {}", name, email);

        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Invalid email format");
        }

        User user = User.builder()
            .name(name.trim())
            .email(email.trim().toLowerCase())
            .createdAt(Instant.now())
            .build();

        return userDao.insert(user);
    }

    public Optional<User> getUser(Long id) {
        logger.info("Fetching user with id: {}", id);
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userDao.findAll();
    }

    public boolean deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        return userDao.deleteById(id);
    }
}

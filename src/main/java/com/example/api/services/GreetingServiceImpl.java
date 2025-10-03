package com.example.api.services;

import jakarta.inject.Singleton;

@Singleton
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}

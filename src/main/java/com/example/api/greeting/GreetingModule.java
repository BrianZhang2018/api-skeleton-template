package com.example.api.greeting;

import com.example.api.services.GreetingService;
import com.example.api.services.GreetingServiceImpl;
import com.google.inject.AbstractModule;

public class GreetingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GreetingService.class).to(GreetingServiceImpl.class);
    }
}

package com.example.api.auth;

import static com.example.api.jaxrs.JaxrsBinder.jaxrsBinder;

import com.example.api.config.JwtConfig;
import com.google.inject.AbstractModule;

public class JwtModule extends AbstractModule {
    @Override
    protected void configure() {
        // Bind JWT configuration
        bind(JwtConfig.class);
        jaxrsBinder(binder()).bind(JwtAuthFilter.class);
    }
}

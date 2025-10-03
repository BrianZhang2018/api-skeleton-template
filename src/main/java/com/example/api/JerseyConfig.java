package com.example.api;

import com.example.api.jaxrs.JaxrsResource;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Jersey ResourceConfig with Guice integration
 * 
 * Automatically registers all JAX-RS resources bound via jaxrsBinder
 * Mimics how Proofpoint Platform's JaxrsModule works
 */
public class JerseyConfig extends ResourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig(Injector injector) {
        // Get all JAX-RS resources bound with @JaxrsResource annotation
        Set<Object> jaxrsResources = injector.getInstance(
                Key.get(new com.google.inject.TypeLiteral<Set<Object>>() {
                }, JaxrsResource.class));

        logger.info("Registering {} JAX-RS resources", jaxrsResources.size());

        // Register each resource instance with Jersey
        jaxrsResources.forEach(resource -> {
            logger.info("Registering resource: {}", resource.getClass().getName());
            register(resource);
        });

        // Register exception mappers, providers, etc.
        packages("com.example.api");
    }
}
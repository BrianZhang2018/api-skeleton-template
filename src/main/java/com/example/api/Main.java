package com.example.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String BASE_URI = "http://0.0.0.0:8080/";

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Starting API Skeleton Template...");

        // Create Guice injector
        Injector injector = Guice.createInjector(new MainModule());
        logger.info("Guice injector created");

        // Create Jersey ResourceConfig with Guice-managed resources
        ResourceConfig config = new JerseyConfig(injector);
        logger.info("Jersey configuration initialized");

        // Start Grizzly HTTP server
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        logger.info("===================================================");
        logger.info("API Skeleton Template started successfully!");
        logger.info("Base URL: {}", BASE_URI);
        logger.info("===================================================");
        logger.info("Available endpoints:");
        logger.info("  GET  {}/api/v1/health", BASE_URI);
        logger.info("  GET  {}/api/v1/users (requires JWT)", BASE_URI);
        logger.info("  POST {}/api/v1/users (requires JWT)", BASE_URI);
        logger.info("  GET  {}/api/v1/users/{{id}} (requires JWT)", BASE_URI);
        logger.info("===================================================");
        logger.info("Press CTRL+C to stop the server...");

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
            server.shutdown();
            logger.info("Server stopped");
        }));

        // Keep server running
        Thread.currentThread().join();
    }
}

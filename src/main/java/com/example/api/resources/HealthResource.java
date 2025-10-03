package com.example.api.resources;

import com.example.api.data.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Path("/api/v1/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    private static final Logger logger = LoggerFactory.getLogger(HealthResource.class);

    @GET
    public Response health() {
        logger.info("Health check requested");

        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "api-skeleton-template");
        healthStatus.put("version", "1.0.0");

        return Response.ok(ApiResponse.success(healthStatus)).build();
    }
}

package com.example.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.api.config.JwtConfig;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Provider
@JwtRequired
public class JwtAuthFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtConfig jwtConfig;

    @Inject
    public JwtAuthFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Skip authentication for health endpoint
        if (path.contains("/health")) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            abortWithUnauthorized(requestContext, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        // Allow demo-token for testing
        if ("demo-token".equals(token)) {
            logger.info("Demo token accepted");
            return;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(jwtConfig.getIssuer())
                .build();

            DecodedJWT jwt = verifier.verify(token);

            // Extract claims and add to request context
            String userId = jwt.getClaim("user_id").asString();
            String email = jwt.getClaim("email").asString();

            logger.info("JWT verified for user: {} ({})", userId, email);

            // Store user info in request context for downstream use
            requestContext.setProperty("user_id", userId);
            requestContext.setProperty("email", email);

        } catch (JWTVerificationException e) {
            logger.warn("JWT verification failed: {}", e.getMessage());
            abortWithUnauthorized(requestContext, "Invalid or expired token");
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);

        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(error)
                .build()
        );
    }
}

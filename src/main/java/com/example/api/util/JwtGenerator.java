package com.example.api.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtGenerator {
    public static void main(String[] args) {
        String secret = System.getenv().getOrDefault("JWT_SECRET", "your-secret-key-change-in-production");
        String issuer = System.getenv().getOrDefault("JWT_ISSUER", "api-skeleton-template");
        long expiration = Long.parseLong(System.getenv().getOrDefault("JWT_EXPIRATION", "86400"));

        String userId = args.length > 0 ? args[0] : "user123";
        String email = args.length > 1 ? args[1] : "user@example.com";

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
            .withIssuer(issuer)
            .withClaim("user_id", userId)
            .withClaim("email", email)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000))
            .sign(algorithm);

        System.out.println("Generated JWT Token:");
        System.out.println(token);
        System.out.println();
        System.out.println("Usage:");
        System.out.println("curl -H \"Authorization: Bearer " + token + "\" http://localhost:8080/api/v1/users");
    }
}

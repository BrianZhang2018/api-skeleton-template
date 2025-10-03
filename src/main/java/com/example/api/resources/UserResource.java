package com.example.api.resources;

import com.example.api.auth.JwtRequired;
import com.example.api.data.ApiResponse;
import com.example.api.data.User;
import com.example.api.exceptions.ValidationException;
import com.example.api.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @JwtRequired
    public Response createUser(Map<String, String> payload) {
        try {
            String name = payload.get("name");
            String email = payload.get("email");

            User user = userService.createUser(name, email);
            return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success(user, "User created successfully"))
                .build();
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.error(e.getMessage()))
                .build();
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error"))
                .build();
        }
    }

    @GET
    @JwtRequired
    public Response getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return Response.ok(ApiResponse.success(users)).build();
        } catch (Exception e) {
            logger.error("Error fetching users", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error"))
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @JwtRequired
    public Response getUser(@PathParam("id") Long id) {
        try {
            Optional<User> user = userService.getUser(id);
            if (user.isPresent()) {
                return Response.ok(ApiResponse.success(user.get())).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
            }
        } catch (Exception e) {
            logger.error("Error fetching user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error"))
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @JwtRequired
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return Response.ok(ApiResponse.success(null, "User deleted successfully")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
            }
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.error("Internal server error"))
                .build();
        }
    }
}

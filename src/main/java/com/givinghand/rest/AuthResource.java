package com.givinghand.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces("application/json")
@Consumes("application/json")
public class AuthResource {
    
    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        if (email == null || email.trim().isEmpty()) {
            return Response.status(400)
                .entity(errorResponse("Email is required", "email"))
                .build();
        }
        
        if (password == null || password.trim().isEmpty()) {
            return Response.status(400)
                .entity(errorResponse("Password is required", "password"))
                .build();
        }
        
        // TODO: Replace with real database authentication later
        Map<String, Object> success = new HashMap<>();
        success.put("message", "Login successful");
        success.put("email", email);
        return Response.ok(success).build();
    }
    
    private Map<String, String> errorResponse(String message, String field) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        error.put("field", field);
        return error;
    }
}

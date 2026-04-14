package com.givinghand.rest;

import com.givinghand.ejb.UserService;
import com.givinghand.model.RegistrationRequest;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/register")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserService userService;

    @POST
    public Response register(RegistrationRequest request) {
        Map<String, String> error = userService.register(request);

        if (error != null) {
            // Return 400 if error
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(error)
                           .build();
        }

        return Response.status(Response.Status.CREATED)
                       .entity(Map.of("message", "User registered successfully."))
                       .build();
    }
}
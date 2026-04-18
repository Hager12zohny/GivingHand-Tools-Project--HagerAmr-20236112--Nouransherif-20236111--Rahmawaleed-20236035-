package com.givinghand.rest;

import com.givinghand.ejb.UserService;
import com.givinghand.model.AppUser;
import com.givinghand.model.UpdateProfileRequest;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/profile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @EJB
    private UserService userService;

    @PUT
    @RolesAllowed({"donor", "organization"})
    public Response updateProfile(UpdateProfileRequest request,
                                  @Context SecurityContext securityContext) {
        try {
            String email = securityContext.getUserPrincipal().getName();
            AppUser user = userService.findByEmail(email);

            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("message", "Unauthorized."))
                        .build();
            }

            Map<String, String> error = userService.updateProfile(
                    user.getId(), request.getName(), request.getBio());

            if (error != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(error)
                        .build();
            }

            return Response.ok(Map.of("message", "Profile updated successfully."))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("message", "An unexpected error occurred."))
                    .build();
        }
    }
}
package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.Exceptions.ArgumentNullException;
import com.v2com.Exceptions.FilterInvalidException;
import com.v2com.Exceptions.UserNotFoundException;
import com.v2com.dto.userDTO;
import com.v2com.service.UserService;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/api/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Transactional
    @PermitAll
    public Response createUser(userDTO userEntity) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created!");
            response.put("user", userService.createUser(userEntity));
            return Response.status(201).entity(response).build();
            
        } catch (ArgumentNullException argumentRequired) {
            return Response.status(406).entity(argumentRequired.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{userId}")
    public Response getUserById(@PathParam("userId") UUID userId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User found!");
            response.put("user", userService.getUserById(userId));
            return Response.ok().entity(response).build();
            
        } catch (UserNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @RolesAllowed("ADMIN")
    public Response getUsersByFilter(@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(userService.getUserByFilters(filters)).build();

        } catch (UserNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (FilterInvalidException invalid) {
            return Response.status(406).entity(invalid.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @RolesAllowed("ADMIN")
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") UUID userId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User deleted!");
            response.put("user", userService.deleteUser(userId));
            return Response.status(410).entity(response).build();

        } catch (UserNotFoundException notFound) {
            return Response.status(404).entity(notFound).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @PermitAll
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") UUID userId, userDTO userDTO) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User updated!");
            response.put("user", userService.updateUser(userId, userDTO));
            return Response.ok(response).build();
            
        } catch (UserNotFoundException notFound) {
            return Response.status(404).entity(notFound).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}
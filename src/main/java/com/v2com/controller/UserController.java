package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.userDTO;
import com.v2com.service.UserService;

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
    public Response createUser(userDTO userEntity) {
        try {
            return Response.ok(userService.createUser(userEntity)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    @Path("/{userId}")
    public Response getUserById(@PathParam("userId") UUID userId) {
        try {
            return Response.ok(userService.getUserById(userId)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Transactional
    public Response getUsersByFilter(@Context UriInfo uriInfo) {
        try {
            Map<String, String> filters = uriInfo.getQueryParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
            return Response.ok(userService.getUserByFilters(filters)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") UUID userId) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User deleted!");
            response.put("user", userService.deleteUser(userId));
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") UUID userId, userDTO userDTO) {
        try {
            return Response.ok(userService.updateUser(userId, userDTO)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}
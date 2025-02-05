package com.v2com.controller;

import java.util.UUID;

import com.v2com.entity.UserEntity;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response createUser(UserEntity userEntity) {
        try {
            return Response.ok(userService.createUser(userEntity)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllUsers() {
        try {
            return Response.ok(userService.getAllUsers()).build();
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
    public Response getUserByFilter(@QueryParam("filterName") String filterName, @QueryParam("filter") String filter) {
        try {
            return Response.ok(userService.getUserByFilter(filterName, filter)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") UUID userId) {
        try {
            return Response.ok(userService.deleteUser(userId)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Transactional
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") UUID userId, UserEntity userEntity) {
        try {
            return Response.ok(userService.updateUser(userId, userEntity)).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}
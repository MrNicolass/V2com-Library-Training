package com.v2com.controller;

import java.util.HashMap;
import java.util.Map;

import com.v2com.Exceptions.EmailNotFoundException;
import com.v2com.Exceptions.PasswordIncorrectException;
import com.v2com.dto.LoginDTO;
import com.v2com.service.AuthService;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @POST
    @Path("login")
    @PermitAll
    public Response login(LoginDTO loginData) throws Exception {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login succeed!");
            response.put("loginToken", authService.login(loginData));
            return Response.ok().entity(response).build();

        } catch (EmailNotFoundException notFound) {
            return Response.status(404).entity(notFound.getMessage()).build();
        } catch (PasswordIncorrectException incorrect) {
            return Response.status(406).entity(incorrect.getMessage()).build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }
    
}
package com.v2com.security.jwt;

import java.time.Duration;

import com.v2com.dto.LoginDTO;
import com.v2com.entity.enums.UserRole;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class JwtTokenGenerator {

    public String generateToken(LoginDTO login, UserRole role) {

        String token = Jwt.issuer("libraryApplication")
            .upn(login.getEmail())
            .groups(role.toString())
            .expiresIn(Duration.ofMinutes(10))
            .sign();

        return token;
    }

}
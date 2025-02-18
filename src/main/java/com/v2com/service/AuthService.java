package com.v2com.service;

import com.v2com.dto.LoginDTO;
import com.v2com.entity.enums.UserRole;
import com.v2com.exceptions.EmailNotFoundException;
import com.v2com.exceptions.PasswordIncorrectException;
import com.v2com.repository.AuthRepository;
import com.v2com.security.jwt.JwtTokenGenerator;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthService {

    private final JwtTokenGenerator generator;
    private final AuthRepository authRepository;

    public AuthService(JwtTokenGenerator generator, AuthRepository authRepository) {
        this.generator = generator;
        this.authRepository = authRepository;
    }
    
    public String login(LoginDTO loginData) throws Exception {
        try {
            String[] userExists = new String[2];
            for (int i = 0; i < userExists.length; i++) {
                userExists[i] = authRepository.findEmail(loginData.getEmail())[i];
            }
            
            if (userExists[0] == null) {
                throw new EmailNotFoundException();
            } else if (userExists[0].equals(loginData.getEmail())) {
                if (authRepository.validatePassword(userExists[0], loginData.getPassword())) {
                    return generator.generateToken(loginData, UserRole.values()[Integer.parseInt(userExists[1])]);
                } else {
                    throw new PasswordIncorrectException();
                }
            } else {
                throw new EmailNotFoundException();
            }

        } catch (EmailNotFoundException notFound) {
            throw notFound;
        } catch (PasswordIncorrectException incorrect) {
            throw incorrect;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }

    }

}
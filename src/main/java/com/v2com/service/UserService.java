package com.v2com.service;

import java.util.List;
import java.util.UUID;

import com.v2com.entity.UserEntity;
import com.v2com.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    //Instance for UserRepository
    private final UserRepository userRepository;

    //Constructor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(UserEntity userEntity) {
        try {
            if (userEntity.getName() == null) {
                throw new IllegalArgumentException("Name is required!");
            }
            else if (userEntity.getPassword() == null) {
                throw new IllegalArgumentException("Password is required!");
            }
            userRepository.persist(userEntity);
            return userEntity;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<UserEntity> getAllUsers() {
        try {
            if(userRepository.findAll().list().isEmpty()) {
                throw new IllegalArgumentException("No users found!");
            } else {
                throw new IllegalArgumentException("No users found!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public UserEntity getUserById(UUID userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId);
            if(userEntity != null) {
                return userEntity;
            } else {
                throw new IllegalArgumentException("User not found!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<UserEntity> getUserByFilter(String filterName, String filter) {
        try {
            List<UserEntity> userEntity = userRepository.find(filterName, filter).list();
            if(userEntity != null) {
                return userEntity;
            } else {
                throw new IllegalArgumentException("User not found!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public UserEntity deleteUser(UUID userId) {
        UserEntity userEntity = this.getUserById(userId);

        if(userEntity != null) {
            try {
                userRepository.delete(userEntity);
                return userEntity;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return userEntity;
        }
    }

    public UserEntity updateUser(UUID userId, UserEntity userEntity) {
        UserEntity user = this.getUserById(userId);

        if (user != null) {
            try {
                user.setUserId(user.getUserId());
                user.setName(userEntity.getName() != null ? userEntity.getName() : user.getName());
                user.setEmail(userEntity.getEmail() != null ? userEntity.getEmail() : user.getEmail());
                user.setPassword(userEntity.getPassword() != null ? userEntity.getPassword() : user.getPassword());
                user.setRole(userEntity.getRole() != null ? userEntity.getRole() : user.getRole());
                return user;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return user;
        }

    }

    
}
package com.v2com.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.dto.userDTO;
import com.v2com.entity.UserEntity;
import com.v2com.repository.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity createUser(userDTO userDTO) {
        try {
            if (userDTO.getName() == null) {
                throw new IllegalArgumentException("Name is required!");
            }
            else if (userDTO.getPassword() == null) {
                throw new IllegalArgumentException("Password is required!");
            }
            
            //Hashes password
            String hashedPassword = BCrypt.withDefaults().hashToString(12, userDTO.getPassword().toCharArray());
            //Creates a new UserEntity by passing the userDTO values
            UserEntity userEntity = new UserEntity(userDTO.getName(), userDTO.getEmail(), hashedPassword, userDTO.getRole());

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
                return userRepository.findAll().list();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public userDTO getUserById(UUID userId) {
        try {
            UserEntity userEntity = userRepository.findById(userId);
            if(userEntity != null) {
                userDTO user = new userDTO(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
                return user;
            } else {
                throw new IllegalArgumentException("User not found!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<UserEntity> getUserByFilters(Map<String, String> filters) {
        List<UserEntity> users = this.getAllUsers();

        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users found!");
        }

        for (String key : filters.keySet()) {
            if (!key.equals("name") && !key.equals("email") && !key.equals("role")) {
                throw new IllegalArgumentException("One or more filters are invalid!");
            }
        }

        /*
        filter.entrySet().stream() = Apply filters to the list of users based on the provided filter
        .reduce() = Reduce the list of users by applying each filter
        -> filteredUsers.stream().filter = Filter the users based on the current filter
        */
        users = filters.entrySet().stream().reduce(users, (filteredUsers, filter) -> filteredUsers.stream().filter(user -> {
                //Catch the filter key and apply the corresponding filter
                switch(filter.getKey()) {
                    case "name":
                        return user.getName().contains(filter.getValue());
                    case "email":
                        return user.getEmail().contains(filter.getValue());
                    case "role":
                        return user.getRole().toString().toUpperCase().contains(filter.getValue());
                    default:
                        return true;
                }
            //Collect the filtered users into a list and combine the results of the reduction
            }).collect(Collectors.toList()), (u1, u2) -> u1);

        return users;
    }

    public userDTO deleteUser(UUID userId) {
        userDTO userDTO = this.getUserById(userId);

        if(userDTO != null) {
            try {
                UserEntity userEntity = new UserEntity(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole());
                userEntity.setUserId(userDTO.getUserId()); //Define user UUID out of constructor
                userRepository.delete(userEntity);
                return userDTO;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            return userDTO;
        }
    }

    public userDTO updateUser(UUID userId, userDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userId);

        if (userEntity != null) {
            try {
                userEntity.setName(userDTO.getName() != null ? userDTO.getName() : userEntity.getName());
                userEntity.setEmail(userDTO.getEmail() != null ? userDTO.getEmail() : userEntity.getEmail());
                userEntity.setRole(userDTO.getRole() != null ? userDTO.getRole() : userEntity.getRole());
                
                userRepository.persist(userEntity);

                return new userDTO(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());

            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("User not found!");
        }

    }

}
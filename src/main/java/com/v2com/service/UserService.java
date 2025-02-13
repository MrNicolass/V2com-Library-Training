package com.v2com.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.v2com.Exceptions.ArgumentNullException;
import com.v2com.Exceptions.FilterInvalidException;
import com.v2com.Exceptions.UserNotFoundException;
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

    public UserEntity createUser(userDTO userDTO) throws Exception {
        try {
            if (userDTO.getName() == null) {
                throw new ArgumentNullException("Name");
            }
            else if (userDTO.getPassword() == null) {
                throw new ArgumentNullException("Password");
            }
            
            //Hashes password
            String hashedPassword = BCrypt.withDefaults().hashToString(12, userDTO.getPassword().toCharArray());
            //Creates a new UserEntity by passing the userDTO values
            UserEntity userEntity = new UserEntity(userDTO.getName(), userDTO.getEmail(), hashedPassword, userDTO.getRole());

            userRepository.persist(userEntity);
            return userEntity;

        } catch (ArgumentNullException arg) {
            throw arg;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<UserEntity> getAllUsers() throws Exception {
        try {
            if(userRepository.findAll().list().isEmpty()) {
                throw new UserNotFoundException();
            } else {
                return userRepository.findAll().list();
            }
        } catch (UserNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public userDTO getUserById(UUID userId) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(userId);

            if(userEntity != null) {
                userDTO user = new userDTO(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
                return user;
            } else {
                throw new UserNotFoundException(userId);
            }
        } catch (UserNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public List<UserEntity> getUserByFilters(Map<String, String> filters) throws Exception {
        try {
            List<UserEntity> users = this.getAllUsers();
    
            if (users.isEmpty()) {
                throw new UserNotFoundException();
            }
    
            for (String key : filters.keySet()) {
                if (!key.equals("name") && !key.equals("email") && !key.equals("role")) {
                    throw new FilterInvalidException(key);
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

        } catch (UserNotFoundException notFound) {
            throw notFound;
        } catch (FilterInvalidException invalid) {
            throw invalid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public userDTO deleteUser(UUID userId) throws Exception {
        try {
            userDTO userDTO = this.getUserById(userId);
    
            if (userDTO != null) {
                UserEntity userEntity = new UserEntity(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole());
                userEntity.setUserId(userDTO.getUserId()); 
                
                userRepository.delete(userEntity);
                return userDTO;
            } else {
                throw new UserNotFoundException(userId);
            }

        } catch (UserNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

    public userDTO updateUser(UUID userId, userDTO userDTO) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(userId);

            if (userEntity != null) {
                userEntity.setName(userDTO.getName() != null ? userDTO.getName() : userEntity.getName());
                userEntity.setEmail(userDTO.getEmail() != null ? userDTO.getEmail() : userEntity.getEmail());
                userEntity.setRole(userDTO.getRole() != null ? userDTO.getRole() : userEntity.getRole());
                
                userRepository.persist(userEntity);

                return new userDTO(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());

            } else {
                throw new UserNotFoundException(userId);
            }
            
        } catch (UserNotFoundException notFound) {
            throw notFound;
        } catch (Exception e) {
            throw new IllegalArgumentException("Something went wrong...: " + e.getMessage());
        }
    }

}
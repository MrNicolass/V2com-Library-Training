package com.v2com.entity;

import java.util.UUID;

import com.v2com.entity.enums.UserRole;

import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Nullable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.inject.Default;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class UserEntity {

    //region Properties
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @NotEmpty
    private String name;

    @Email
    private String email = "";


    @NotEmpty
    private String password;

    @Default
    private UserRole role = UserRole.USER;

    //endregion

    //region Constructors

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    //endregion

    //region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    //endregion

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
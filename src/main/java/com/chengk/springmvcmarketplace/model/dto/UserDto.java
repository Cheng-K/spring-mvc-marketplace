package com.chengk.springmvcmarketplace.model.dto;

import java.util.Set;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {
    private Integer id;
    @NotBlank(message = "Username cannot be blank")
    @Length(min = 6, max = 50, message = "Username can only be within 6 to 50 characters")
    private String username;
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 6, max = 12, message = "Password can only be within 6 to 12 characters")
    private String password;
    private Set<RoleDto> roles;

    public UserDto(Integer id, String username, String email, Set<RoleDto> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

}

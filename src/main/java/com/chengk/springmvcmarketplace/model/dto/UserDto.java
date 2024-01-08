package com.chengk.springmvcmarketplace.model.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {
    private Integer id;
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 50, message = "Username can only be within 4 to 50 characters")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String email;
    @JsonIgnore
    private String password;
    private Set<RoleDto> roles = new HashSet<>();
    @JsonIgnore
    private MultipartFile uploadedProfilePicture;
    private String profilePicturePath;

    public UserDto(Integer id, String username, String email, Set<RoleDto> roles, String profilePicturePath) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.profilePicturePath = profilePicturePath;
    }

    public UserDto() {
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

    @JsonIgnore
    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return roles.stream().map((roleDto) -> new SimpleGrantedAuthority(roleDto.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public MultipartFile getUploadedProfilePicture() {
        return uploadedProfilePicture;
    }

    public void setUploadedProfilePicture(MultipartFile uploadedImage) {
        this.uploadedProfilePicture = uploadedImage;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

}

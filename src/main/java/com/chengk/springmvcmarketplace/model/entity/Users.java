package com.chengk.springmvcmarketplace.model.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("Users")
public class Users {
    @Id
    private Integer id;
    private String username;
    private String email;
    private String password;
    @MappedCollection(idColumn = "user_id")
    private Set<RoleRef> roles = new HashSet<>();
    private String profilePicture;

    public Users(Integer id, String username, String email, String password, Set<RoleRef> roles,
            String profilePicture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.profilePicture = profilePicture;
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

    public Set<RoleRef> getRoles() {
        return roles;
    }

    public List<Integer> getRolesId() {
        return roles.stream().map((ref) -> ref.getRoleId()).toList();
    }

    public void addRole(Roles role) {
        roles.add(new RoleRef(role.getId()));
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}

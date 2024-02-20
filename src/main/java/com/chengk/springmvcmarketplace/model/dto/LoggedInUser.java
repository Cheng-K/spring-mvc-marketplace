package com.chengk.springmvcmarketplace.model.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoggedInUser extends User {

    private String profilePicture;
    private Integer id;

    public LoggedInUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
            String profilePicture, Integer id) {
        super(username, password, authorities);
        this.profilePicture = profilePicture;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

}

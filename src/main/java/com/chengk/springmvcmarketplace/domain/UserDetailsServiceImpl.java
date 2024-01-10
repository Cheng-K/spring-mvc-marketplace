package com.chengk.springmvcmarketplace.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.LoggedInUser;
import com.chengk.springmvcmarketplace.model.entity.Roles;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.RolesRepository;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> result = usersRepository.findByUsername(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        Users user = result.get();
        Iterable<Roles> roles = rolesRepository.findAllById(user.getRolesId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (var role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new LoggedInUser(username, user.getPassword(), authorities, user.getProfilePicture());
    }
}

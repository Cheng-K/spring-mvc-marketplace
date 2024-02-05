package com.chengk.springmvcmarketplace.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.RoleDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.RoleRef;
import com.chengk.springmvcmarketplace.model.entity.Roles;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.RolesRepository;

@Service
public class UserDtoConverter implements DtoConverter<Users, UserDto> {

    private RolesRepository rolesRepository;
    private DtoConverter<Roles, RoleDto> roleDtoConverter;
    private PasswordEncoder passwordEncoder;

    public UserDtoConverter(RolesRepository rolesRepository, DtoConverter<Roles, RoleDto> roleDtoConverter,
            PasswordEncoder passwordEncoder) {
        this.rolesRepository = rolesRepository;
        this.roleDtoConverter = roleDtoConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto convertToDto(Users element) {
        Iterable<Roles> roles = rolesRepository.findAllById(element.getRolesId());
        Set<RoleDto> roleDto = new HashSet<>();
        for (var role : roles) {
            roleDto.add(roleDtoConverter.convertToDto(role));
        }
        return new UserDto(element.getId(), element.getUsername(), element.getEmail(), roleDto,
                element.getProfilePicture());
    }

    @Override
    public Users convertToEntity(UserDto element) {
        String password = (element.getPassword() == null || element.getPassword().isEmpty()) ? null
                : passwordEncoder.encode(element.getPassword());
        Set<RoleRef> roleRefs = element.getRoles().stream().map((role) -> new RoleRef(role.getId()))
                .collect(Collectors.toSet());
        return new Users(element.getId(), element.getUsername(), element.getEmail(), password, roleRefs,
                element.getProfilePicturePath(), null);
    }

}

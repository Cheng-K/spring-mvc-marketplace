package com.chengk.springmvcmarketplace.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.RoleDto;
import com.chengk.springmvcmarketplace.model.entity.Roles;
import com.chengk.springmvcmarketplace.repository.RolesRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private DtoConverter<Roles, RoleDto> dtoConverter;
    private RolesRepository rolesRepository;

    public RoleServiceImpl(DtoConverter<Roles, RoleDto> dtoConverter, RolesRepository rolesRepository) {
        this.dtoConverter = dtoConverter;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Optional<Roles> result = rolesRepository.findByName(name);
        if (result.isEmpty())
            return null;
        return dtoConverter.convertToDto(result.get());
    }
}

package com.chengk.springmvcmarketplace.domain;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.RoleDto;
import com.chengk.springmvcmarketplace.model.entity.Roles;

@Service
public class RoleDtoConverter implements DtoConverter<Roles, RoleDto> {

    @Override
    public RoleDto convertToDto(Roles element) {
        return new RoleDto(element.getId(), element.getName());
    }

    @Override
    public Roles convertToEntity(RoleDto element) {
        return new Roles(element.getId(), element.getName());
    }

}

package com.chengk.springmvcmarketplace.domain;

import com.chengk.springmvcmarketplace.model.dto.RoleDto;

public interface RoleService {
    RoleDto getRoleByName(String name);
}

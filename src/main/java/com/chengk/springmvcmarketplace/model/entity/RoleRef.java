package com.chengk.springmvcmarketplace.model.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("User_Roles")
public class RoleRef {
    private Integer roleId;

    public RoleRef(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

}

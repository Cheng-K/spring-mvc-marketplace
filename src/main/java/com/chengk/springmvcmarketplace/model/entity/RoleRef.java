package com.chengk.springmvcmarketplace.model.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("User_Roles")
public class RoleRef {
    private int roleId;

    public RoleRef(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}

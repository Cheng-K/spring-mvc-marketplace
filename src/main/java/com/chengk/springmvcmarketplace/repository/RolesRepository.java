package com.chengk.springmvcmarketplace.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Roles;

@Repository
public interface RolesRepository extends CrudRepository<Roles, Integer> {

}

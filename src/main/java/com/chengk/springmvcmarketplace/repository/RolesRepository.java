package com.chengk.springmvcmarketplace.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Roles;

@Repository
public interface RolesRepository extends CrudRepository<Roles, Integer> {
    Optional<Roles> findByName(String name);
}

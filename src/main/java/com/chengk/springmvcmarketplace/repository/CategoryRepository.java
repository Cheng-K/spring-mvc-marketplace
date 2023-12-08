package com.chengk.springmvcmarketplace.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Categories;

@Repository
public interface CategoryRepository extends CrudRepository<Categories, Integer> {
}

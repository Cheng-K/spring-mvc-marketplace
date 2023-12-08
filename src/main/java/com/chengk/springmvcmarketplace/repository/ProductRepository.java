package com.chengk.springmvcmarketplace.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Products;

@Repository
public interface ProductRepository extends CrudRepository<Products, Integer> {
}

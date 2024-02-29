package com.chengk.springmvcmarketplace.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chengk.springmvcmarketplace.model.entity.Cart;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

}

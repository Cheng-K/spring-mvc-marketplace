package com.chengk.springmvcmarketplace.model.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("Cart")
public class Cart {
    @Id
    private Integer id;
    @MappedCollection(idColumn = "cart_id")
    private Set<CartProductRef> products = new HashSet<>();

    public Cart(Integer id, Set<CartProductRef> products) {
        this.id = id;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<CartProductRef> getProducts() {
        return products;
    }

    public void addProduct(Products product) {
        products.add(new CartProductRef(product.getId()));

    }

}

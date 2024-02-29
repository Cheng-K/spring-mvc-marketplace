package com.chengk.springmvcmarketplace.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CartDto {
    private Integer id;
    private List<ProductDto> products;

    public CartDto() {
    }

    public CartDto(Integer id) {
        this.id = id;
        this.products = new ArrayList<>();
    }

    public CartDto(Integer id, List<ProductDto> products) {
        this.id = id;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void addProduct(ProductDto productDto) {
        products.add(productDto);
    }
}

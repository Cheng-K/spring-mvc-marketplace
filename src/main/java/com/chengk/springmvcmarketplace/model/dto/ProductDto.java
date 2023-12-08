package com.chengk.springmvcmarketplace.model.dto;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.chengk.springmvcmarketplace.model.entity.Categories;
import com.chengk.springmvcmarketplace.model.entity.ProductImages;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.model.value_objects.Condition;

public class ProductDto {
    public Integer id;
    public String title;
    public String description;
    public CategoryDto category;
    public Timestamp listedOn;
    public Condition condition;
    public Set<ProductImageDto> images = new HashSet<>();

    public ProductDto() {
    }

    public ProductDto(Products product) {
        id = product.getId();
        title = product.getTitle();
        description = product.getDescription();
        category = new CategoryDto(product.getCategory());
        listedOn = product.getListedOn();
        condition = product.getCondition();
        for (var image : product.getImages()) {
            images.add(new ProductImageDto(image));
        }
    }

}

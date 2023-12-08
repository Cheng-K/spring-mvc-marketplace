package com.chengk.springmvcmarketplace.model.dto;

import com.chengk.springmvcmarketplace.model.entity.ProductImages;

public class ProductImageDto {
    public Integer id;
    public String path;

    public ProductImageDto(ProductImages image) {
        this.id = image.getId();
        this.path = image.getPath();
    }

    public ProductImageDto() {
    }

}

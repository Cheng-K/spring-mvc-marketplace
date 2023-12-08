package com.chengk.springmvcmarketplace.model.dto;

import com.chengk.springmvcmarketplace.model.entity.Categories;

public class CategoryDto {
    private Integer id;
    private String title;

    public CategoryDto(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public CategoryDto() {
    }

    public CategoryDto(Categories category) {
        id = category.getId();
        title = category.getTitle();
    }

}

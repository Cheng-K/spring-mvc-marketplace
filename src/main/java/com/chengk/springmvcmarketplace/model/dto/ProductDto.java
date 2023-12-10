package com.chengk.springmvcmarketplace.model.dto;

import java.sql.Timestamp;

import com.chengk.springmvcmarketplace.model.value_objects.Condition;

public class ProductDto {
    private Integer id;
    private String title;
    private String description;
    private CategoryDto category;
    private Timestamp listedOn;
    private Condition condition;
    private String image;

    public ProductDto() {
    }

    public ProductDto(Integer id, String title, String description, CategoryDto category, Timestamp listedOn,
            Condition condition, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.listedOn = listedOn;
        this.condition = condition;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Timestamp getListedOn() {
        return listedOn;
    }

    public void setListedOn(Timestamp listedOn) {
        this.listedOn = listedOn;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

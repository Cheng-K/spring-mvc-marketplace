package com.chengk.springmvcmarketplace.model.entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.chengk.springmvcmarketplace.model.value_objects.Condition;

@Table("Products")
public class Products {
    @Id
    private Integer id;
    private String title;
    private Double price;
    private String description;
    private Integer categoryId;
    private Timestamp listedOn;
    private Condition condition;
    private String image;

    public Products(Integer id, String title, Double price, String description, Integer categoryId, Timestamp listedOn,
            Condition condition, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.listedOn = listedOn;
        this.condition = condition;
        this.image = image;
        this.price = price;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getImages() {
        return image;
    }

    public void setImages(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

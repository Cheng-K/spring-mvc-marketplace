package com.chengk.springmvcmarketplace.model.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import com.chengk.springmvcmarketplace.model.value_objects.Condition;

@Table("Products")
public class Products {
    @Id
    private Integer id;
    private String title;
    private String description;
    private Categories category;
    private Timestamp listedOn;
    private Condition condition;
    @MappedCollection(idColumn = "product_id")
    private Set<ProductImages> images = new HashSet<>();

    public Products(Integer id, String title, String description, Categories category, Timestamp listedOn,
            Condition condition, Set<ProductImages> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.listedOn = listedOn;
        this.condition = condition;
        this.images = images;
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

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
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

    public Set<ProductImages> getImages() {
        return images;
    }

    public void setImages(Set<ProductImages> images) {
        this.images = images;
    }

}

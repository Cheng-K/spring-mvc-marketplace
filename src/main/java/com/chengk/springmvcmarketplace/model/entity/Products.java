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
    public Integer id;
    public String title;
    public String description;
    public Categories category;
    public Timestamp listedOn;
    public Condition condition;
    @MappedCollection(idColumn = "product_id")
    public Set<ProductImages> images = new HashSet<>();
}

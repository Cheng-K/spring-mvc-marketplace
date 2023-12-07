package com.chengk.springmvcmarketplace.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ProductImages")
public class ProductImages {
    @Id
    public Integer id;
    public String path;
}

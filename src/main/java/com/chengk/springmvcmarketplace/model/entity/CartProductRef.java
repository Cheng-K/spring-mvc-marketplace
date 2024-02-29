package com.chengk.springmvcmarketplace.model.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("Cart_Products")
public class CartProductRef {
    private Integer productId;

    public CartProductRef(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}

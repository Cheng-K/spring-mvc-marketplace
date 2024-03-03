package com.chengk.springmvcmarketplace.model.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // different object type or null
        }
        CartProductRef other = (CartProductRef) obj;
        return productId == other.productId; // compare id fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId); // use Objects.hash for consistent hashing
    }

}

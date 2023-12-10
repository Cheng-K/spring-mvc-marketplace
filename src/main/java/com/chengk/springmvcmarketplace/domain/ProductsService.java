package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;

public interface ProductsService {
    public List<ProductDto> getAllProducts();
}

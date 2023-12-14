package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;

public interface ProductsService {
    public List<ProductDto> getAllProducts();

    public void addNewProduct(ProductDto productDto);

    public ProductDto getProductById(Integer productId);
}

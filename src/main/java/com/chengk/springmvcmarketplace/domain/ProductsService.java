package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;

public interface ProductsService {
    List<ProductDto> getAllProducts();

    void addNewProduct(ProductDto productDto);

    ProductDto getProductById(Integer productId);

    void editProduct(ProductDto productDto);

    void removeProduct(Integer productId);

    List<ProductDto> getProductsByQuery(String query);

}

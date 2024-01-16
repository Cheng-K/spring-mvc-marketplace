package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;

public interface ProductsService {
    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsWithSort(Sort sort);

    void addNewProduct(ProductDto productDto);

    ProductDto getProductById(Integer productId);

    void editProduct(ProductDto productDto);

    void removeProduct(Integer productId);

    List<ProductDto> getProductsByQuery(String query);

    List<ProductDto> getProductsByQueryWithSort(String query, Sort sort);

    List<ProductDto> getProductsByCategoryId(Integer categoryId);

}

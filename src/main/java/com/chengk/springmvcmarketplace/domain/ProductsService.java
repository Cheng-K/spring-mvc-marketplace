package com.chengk.springmvcmarketplace.domain;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;

public interface ProductsService {

    Slice<ProductDto> getAllProductsWithPagination(int pageNumber);

    Slice<ProductDto> getAllProductsWithSort(Sort sort, int pageNumber);

    void addNewProduct(ProductDto productDto);

    ProductDto getProductById(Integer productId);

    void editProduct(ProductDto productDto);

    void removeProduct(Integer productId);

    Slice<ProductDto> getProductsByQuery(String query, int pageNumber);

    Slice<ProductDto> getProductsByQueryWithSort(String query, Sort sort, int pageNumber);

    Slice<ProductDto> getProductsByCategoryId(Integer categoryId, int pageNumber);

}

package com.chengk.springmvcmarketplace.domain;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.chengk.springmvcmarketplace.model.dto.CartDto;
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

    Slice<ProductDto> getAllUserProductsWithPagination(int pageNumber, int userId);

    Slice<ProductDto> getAllUserProductsWithSort(Sort sort, int pageNumber, int userId);

    Slice<ProductDto> getUserProductsByQuery(String query, int pageNumber, int userId);

    Slice<ProductDto> getUserProductsByQueryOrderByLatest(String query, int pageNumber, int userId);

    Slice<ProductDto> getUserProductsByQueryOrderByPrice(String query, int pageNumber, int userId, boolean ascending);

    Slice<ProductDto> getUserProductsByCategoryId(Integer categoryId, int pageNumber, int userId);

    CartDto getUserShoppingCart(Integer userId);
}

package com.chengk.springmvcmarketplace.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.repository.ProductRepository;

@Service
public class ProductsServiceImpl implements ProductsService {

    private ProductRepository productRepository;
    private DtoConverter<Products, ProductDto> productDtoConverter;

    public ProductsServiceImpl(ProductRepository productRepository,
            DtoConverter<Products, ProductDto> productDtoConverter) {
        this.productRepository = productRepository;
        this.productDtoConverter = productDtoConverter;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<ProductDto> result = new ArrayList<>();
        Iterable<Products> products = productRepository.findAll();
        for (var product : products) {
            result.add(productDtoConverter.convert(product));
        }
        return result;
    }

}

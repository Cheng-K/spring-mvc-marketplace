package com.chengk.springmvcmarketplace.domain;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.entity.Categories;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.repository.CategoryRepository;

@Service
public class ProductDtoConverter implements DtoConverter<Products, ProductDto> {

    private CategoryRepository categoryRepository;

    public ProductDtoConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDto convertToDto(Products element) {
        Categories category = categoryRepository.findById(element.getCategoryId()).orElse(null);
        return new ProductDto(
                element.getId(),
                element.getTitle(),
                element.getDescription(),
                new CategoryDto(category.getId(), category.getTitle(), category.getCreatedOn(),
                        category.getLastUpdated()),
                element.getListedOn(),
                element.getCondition(),
                element.getImages());
    }

    @Override
    public Products convertToEntity(ProductDto element) {
        return new Products(element.getId(), element.getTitle(), element.getDescription(),
                element.getCategory().getId(), element.getListedOn(), element.getCondition(), element.getImage());
    }

}

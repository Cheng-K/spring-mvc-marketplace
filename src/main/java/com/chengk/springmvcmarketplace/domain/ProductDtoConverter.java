package com.chengk.springmvcmarketplace.domain;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.Categories;
import com.chengk.springmvcmarketplace.model.entity.Products;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.CategoryRepository;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

@Service
public class ProductDtoConverter implements DtoConverter<Products, ProductDto> {

    private CategoryRepository categoryRepository;
    private UsersRepository usersRepository;
    private DtoConverter<Users, UserDto> userDtoConverter;

    public ProductDtoConverter(CategoryRepository categoryRepository, UsersRepository usersRepository,
            DtoConverter<Users, UserDto> userDtoConverter) {
        this.categoryRepository = categoryRepository;
        this.usersRepository = usersRepository;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public ProductDto convertToDto(Products element) {
        Categories category = categoryRepository.findById(element.getCategoryId()).orElse(null);
        Users user = usersRepository.findById(element.getSellerId()).orElse(null);
        return new ProductDto(
                element.getId(),
                element.getTitle(),
                element.getPrice(),
                element.getDescription(),
                new CategoryDto(category.getId(), category.getTitle(), category.getCreatedOn(),
                        category.getLastUpdated()),
                element.getListedOn(),
                element.getCondition(),
                element.getImages(),
                userDtoConverter.convertToDto(user));
    }

    @Override
    public Products convertToEntity(ProductDto element) {
        return new Products(element.getId(), element.getTitle(), element.getPrice(), element.getDescription(),
                element.getCategory().getId(), element.getListedOn(), element.getCondition(), element.getImage(),
                element.getSeller().getId());
    }

}

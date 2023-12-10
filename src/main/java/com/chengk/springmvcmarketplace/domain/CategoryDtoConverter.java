package com.chengk.springmvcmarketplace.domain;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.entity.Categories;

@Service
public class CategoryDtoConverter implements DtoConverter<Categories, CategoryDto> {

    @Override
    public CategoryDto convertToDto(Categories element) {
        return new CategoryDto(element.getId(), element.getTitle(), element.getCreatedOn(), element.getLastUpdated());
    }

    @Override
    public Categories convertToEntity(CategoryDto element) {
        return new Categories(element.getId(), element.getTitle(), element.getCreatedOn(), element.getLastUpdated());
    }

}

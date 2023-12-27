package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

    void addNewCategory(CategoryDto categoryDto);

    boolean doesCategoryExists(String categoryName);
}

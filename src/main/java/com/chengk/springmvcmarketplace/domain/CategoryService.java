package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;

public interface CategoryService {
    public List<CategoryDto> getAllCategories();
}
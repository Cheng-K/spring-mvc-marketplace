package com.chengk.springmvcmarketplace.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.entity.Categories;
import com.chengk.springmvcmarketplace.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private DtoConverter<Categories, CategoryDto> categoryDtoConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
            DtoConverter<Categories, CategoryDto> categoryDtoConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> result = new ArrayList<>();
        for (var category : categoryRepository.findAll()) {
            result.add(categoryDtoConverter.convertToDto(category));
        }
        return result;
    }

    @Override
    public void addNewCategory(CategoryDto categoryDto) {
        categoryRepository.save(categoryDtoConverter.convertToEntity(categoryDto));
    }

    @Override
    public boolean doesCategoryExists(String categoryName) {
        return categoryRepository.existsByTitle(categoryName);
    }

    @Override
    public void removeCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public void editCategory(CategoryDto categoryDto) {
        categoryDto.setLastUpdated(LocalDateTime.now());
        categoryRepository.save(categoryDtoConverter.convertToEntity(categoryDto));
    }

}

package com.chengk.springmvcmarketplace.domain;

import java.util.Locale;

import org.springframework.format.Formatter;

import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CategoryDtoFormatter implements Formatter<CategoryDto> {

    @Override
    public String print(CategoryDto object, Locale locale) {
        return object.toString();
    }

    @Override
    public CategoryDto parse(String text, Locale locale) {
        ObjectMapper mapper = new ObjectMapper();
        CategoryDto parsed = null;
        try {
            parsed = mapper.readValue(text, CategoryDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return parsed;
    }

}

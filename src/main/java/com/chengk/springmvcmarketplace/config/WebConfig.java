package com.chengk.springmvcmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chengk.springmvcmarketplace.domain.CategoryDtoFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatter(categoryDtoFormatter());
    }

    @Bean
    public CategoryDtoFormatter categoryDtoFormatter() {
        return new CategoryDtoFormatter();
    }
}

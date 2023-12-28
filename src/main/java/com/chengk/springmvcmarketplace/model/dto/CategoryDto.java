package com.chengk.springmvcmarketplace.model.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
    private Integer id;
    @NotBlank(message = "Category title must not be blank")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    public CategoryDto(Integer id, String title, LocalDateTime createdOn, LocalDateTime lastUpdated) {
        this.id = id;
        this.title = title;
        this.createdOn = createdOn;
        this.lastUpdated = lastUpdated;
    }

    public CategoryDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}

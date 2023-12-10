package com.chengk.springmvcmarketplace.model.dto;

public class CategoryDto {
    private Integer id;
    private String title;

    public CategoryDto(Integer id, String title) {
        this.id = id;
        this.title = title;
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

}

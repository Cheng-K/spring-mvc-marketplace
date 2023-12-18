package com.chengk.springmvcmarketplace.model.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.chengk.springmvcmarketplace.model.value_objects.Condition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {
    private Integer id;
    @NotBlank(message = "Product title must not be blank")
    @Size(max = 100, message = "Product title must not exceed 100 characters")
    private String title;
    @DecimalMin(value = "0.0", message = "Product price must not be a negative decimal")
    @DecimalMax(value = "9999.99", message = "Product price must not be greater than 9999")
    @NotNull(message = "Product price must be specified")
    private Double price;
    @NotBlank(message = "Product description must not be blank")
    private String description;
    @NotNull
    @Valid
    private CategoryDto category;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime listedOn;
    @NotNull(message = "Product's condition must be specified")
    private Condition condition;
    private String image;
    @JsonIgnore
    private MultipartFile imageFile;

    public ProductDto() {
    }

    public ProductDto(Integer id, String title, Double price, String description, CategoryDto category,
            LocalDateTime listedOn,
            Condition condition, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.listedOn = listedOn;
        this.condition = condition;
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public LocalDateTime getListedOn() {
        return listedOn;
    }

    public void setListedOn(LocalDateTime listedOn) {
        this.listedOn = listedOn;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

}

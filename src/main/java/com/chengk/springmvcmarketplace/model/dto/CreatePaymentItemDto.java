package com.chengk.springmvcmarketplace.model.dto;

public class CreatePaymentItemDto {
    private String id;

    public CreatePaymentItemDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

package com.chengk.springmvcmarketplace.model.dto;

public class CreatePaymentResponseDto {
    private String clientSecret;

    public CreatePaymentResponseDto(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

}

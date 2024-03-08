package com.chengk.springmvcmarketplace.model.dto;

import java.util.List;

public class CreatePaymentDto {
    private List<CreatePaymentItemDto> items;

    public CreatePaymentDto(List<CreatePaymentItemDto> items) {
        this.items = items;
    }

    public List<CreatePaymentItemDto> getItems() {
        return items;
    }

}

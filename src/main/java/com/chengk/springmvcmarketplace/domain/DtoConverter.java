package com.chengk.springmvcmarketplace.domain;

public interface DtoConverter<T, U> {
    public U convertToDto(T element);

    public T convertToEntity(U element);
}

package com.chengk.springmvcmarketplace.domain;

public interface DtoConverter<T, U> {
    public U convert(T element);
}

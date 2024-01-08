package com.chengk.springmvcmarketplace.model.dto;

import org.springframework.http.HttpStatus;

public class HttpErrorDto {
    private HttpStatus httpStatus;
    private String title;
    private String subtitle;

    public HttpErrorDto(HttpStatus httpStatus, String title, String subtitle) {
        this.httpStatus = httpStatus;
        this.title = title;
        this.subtitle = subtitle;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public static HttpErrorDto createProductNotFoundError() {
        return new HttpErrorDto(HttpStatus.NOT_FOUND, "Product Not Found",
                "Unable to find the product.");
    }

}

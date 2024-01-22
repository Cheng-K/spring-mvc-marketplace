package com.chengk.springmvcmarketplace.domain.exceptions;

import org.springframework.web.server.ResponseStatusException;

import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;

public class AppResponseException extends ResponseStatusException {

    private HttpErrorDto errorDetails;

    public AppResponseException(HttpErrorDto httpError) {
        super(httpError.getHttpStatus(), httpError.getTitle());
        this.errorDetails = httpError;
    }

    public HttpErrorDto getErrorDetails() {
        return errorDetails;
    }

}

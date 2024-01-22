package com.chengk.springmvcmarketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;

@ControllerAdvice
public class AppErrorController {
    @ExceptionHandler({ AppResponseException.class })
    public String handleResponseStatusException(AppResponseException ex, Model model) {
        model.addAttribute("error", ex.getErrorDetails());
        return "http-error";
    }

    @ExceptionHandler({ Exception.class })
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", new HttpErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred",
                "An unexpected error is encountered. Please try again."));
        return "http-error";
    }

}

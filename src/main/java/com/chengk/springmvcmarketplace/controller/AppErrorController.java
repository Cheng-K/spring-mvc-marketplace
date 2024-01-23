package com.chengk.springmvcmarketplace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;

@ControllerAdvice
public class AppErrorController {
    Logger logger = LoggerFactory.getLogger(AppErrorController.class);

    @ExceptionHandler({ AppResponseException.class })
    public String handleResponseStatusException(AppResponseException ex, Model model) {
        logger.error(ex.getErrorDetails().getSubtitle(), ex);
        model.addAttribute("error", ex.getErrorDetails());
        return "http-error";
    }

    @ExceptionHandler({ Exception.class })
    public String handleGenericException(Exception ex, Model model) {
        logger.error(ex.getMessage(), ex);
        model.addAttribute("error", new HttpErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred",
                "An unexpected error is encountered. Please try again."));
        return "http-error";
    }

}

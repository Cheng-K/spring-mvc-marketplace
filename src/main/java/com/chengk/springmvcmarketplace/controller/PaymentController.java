package com.chengk.springmvcmarketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.CreatePaymentDto;
import com.chengk.springmvcmarketplace.model.dto.CreatePaymentResponseDto;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Controller("/payment")
public class PaymentController {
    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponseDto> createPaymentIntent(@RequestBody CreatePaymentDto payment) {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(100l)
                .setCurrency("myr")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build())
                .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.create(params);
            CreatePaymentResponseDto paymentResponse = new CreatePaymentResponseDto(paymentIntent.getClientSecret());
            return ResponseEntity.ok().body(paymentResponse);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new AppResponseException(
                    new HttpErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong",
                            "Please contact customer support to check why payment request is declined"));
        }
    }

}

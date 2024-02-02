package com.chengk.springmvcmarketplace.model.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

public class PasswordChangeDto {
    @NotEmpty(message = "Password cannot be blank")
    @Length(min = 6, max = 14, message = "Password length must be within 6 to 14 characters")
    private String newPassword;
    @NotEmpty(message = "Password cannot be blank")
    @Length(min = 6, max = 14, message = "Password length must be within 6 to 14 characters")
    private String repeatNewPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }

}

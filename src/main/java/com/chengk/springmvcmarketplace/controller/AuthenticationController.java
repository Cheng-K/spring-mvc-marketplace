
package com.chengk.springmvcmarketplace.controller;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.chengk.springmvcmarketplace.domain.EmailService;
import com.chengk.springmvcmarketplace.domain.RoleService;
import com.chengk.springmvcmarketplace.domain.TokenService;
import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;
import com.chengk.springmvcmarketplace.model.dto.PasswordChangeDto;
import com.chengk.springmvcmarketplace.model.dto.RoleDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    private UserService userService;
    private RoleService roleService;
    private TokenService tokenService;
    private EmailService emailService;
    private TemplateEngine templateEngine;

    public AuthenticationController(UserService userService, RoleService roleService, TokenService tokenService,
            EmailService emailService, TemplateEngine templateEngine) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("newUser", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String postNewUser(@Valid @ModelAttribute("newUser") UserDto userDto, BindingResult bindingResult,
            Model model) {
        if (userDto.getEmail() != null && userService.emailExists(userDto.getEmail())) {
            bindingResult.rejectValue("email", "email.exists",
                    "Email entered has already been used by another account. Please provide another one");
        }
        if (userDto.getUsername() != null && userService.usernameExists(userDto.getUsername())) {
            bindingResult.rejectValue("username", "username.exists",
                    "Username entered has already been used by another account. Please provide another one");
        }
        if (userDto.getPassword().isEmpty()) {
            bindingResult.rejectValue("password", "password.empty", "Password cannot be blank");
        } else if (userDto.getPassword().length() < 6) {
            bindingResult.rejectValue("password", "password.short", "Password must be at least 6 characters");
        } else if (userDto.getPassword().length() > 14) {
            bindingResult.rejectValue("password", "password.long", "Password must not exceed 14 characters");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        RoleDto userRole = roleService.getRoleByName("USER");
        if (userRole == null) {
            throw new RuntimeException("No suitable role found in db");
        }
        userDto.getRoles().add(userRole);
        userService.addNewUser(userDto);
        return "register-successful";
    }

    @GetMapping("/reset-password")
    public String getResetPassword(Model model) {
        model.addAttribute("resetUser", new UserDto());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String postResetPassword(@ModelAttribute("resetUser") @Valid UserDto resetUser, BindingResult bindingResult,
            HttpServletRequest request) {
        if (bindingResult.hasFieldErrors()) {
            return "reset-password";
        }

        UserDto user = userService.getUserByUsernameAndEmail(resetUser.getUsername(), resetUser.getEmail());
        if (user == null) {
            bindingResult.rejectValue("username", "username.not.found",
                    "User not found");
            bindingResult.rejectValue("email", "email.not.found", "User not found");
        }

        if (bindingResult.hasFieldErrors()) {
            return "reset-password";
        }

        String token = userService.getResetPasswordToken(user);
        String url = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/change-password")
                .replaceQuery(MessageFormat.format("token={0}", token)).toUriString();
        Context context = new Context();
        context.setVariable("password_reset_link", url);
        String parsedEmailContent = templateEngine.process("reset-password-email", context);
        emailService.send("noreply@marketplace.com", user.getEmail(), "Password Reset Request",
                parsedEmailContent);
        return "reset-password-successful";
    }

    @GetMapping("/change-password")
    public String getChangePassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("passwordChange", new PasswordChangeDto());
        model.addAttribute("passwordChangePostURL", ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String postChangePassword(@RequestParam("token") String token,
            @ModelAttribute("passwordChange") @Valid PasswordChangeDto passwordChangeDto, BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()
                && !passwordChangeDto.getNewPassword().contentEquals(passwordChangeDto.getRepeatNewPassword())) {
            bindingResult.rejectValue("repeatNewPassword", "password.not.match",
                    "New password does not match the repeated password entered.");
        }
        if (bindingResult.hasErrors()) {
            return "change-password";
        }
        String userId = tokenService.verifyAndGetClaim(token, "uid");
        if (!userService.verifyPasswordResetToken(Integer.parseInt(userId), token)) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to reset password", "Please request a new password reset link and try again."));
        }
        UserDto user = userService.getUserById(Integer.parseInt(userId));
        user.setPassword(passwordChangeDto.getNewPassword());
        userService.editUser(user);
        return "change-password-successful";
    }

}

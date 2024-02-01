
package com.chengk.springmvcmarketplace.controller;

import java.text.MessageFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chengk.springmvcmarketplace.domain.EmailService;
import com.chengk.springmvcmarketplace.domain.RoleService;
import com.chengk.springmvcmarketplace.domain.TokenService;
import com.chengk.springmvcmarketplace.domain.UserService;
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

    public AuthenticationController(UserService userService, RoleService roleService, TokenService tokenService,
            EmailService emailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.emailService = emailService;
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
    public String testGetToken(Model model) {
        model.addAttribute("resetUser", new UserDto());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String postMethodName(@ModelAttribute("resetUser") @Valid UserDto resetUser, BindingResult bindingResult,
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

        String token = tokenService.generateTokenForUser(user.getId().toString());
        String url = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/change-password")
                .replaceQuery(MessageFormat.format("token={0}", token)).toUriString();
        emailService.send("noreply@marketplace.com", "randomguy@mail.com", "Password reset", "URL: " + url);
        return "reset-password-successful";
    }

}

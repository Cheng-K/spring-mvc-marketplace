
package com.chengk.springmvcmarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.chengk.springmvcmarketplace.domain.RoleService;
import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.model.dto.RoleDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;

import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    private UserService userService;
    private RoleService roleService;

    public AuthenticationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

}

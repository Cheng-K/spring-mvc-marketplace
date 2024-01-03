package com.chengk.springmvcmarketplace.controller;

import java.text.MessageFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.model.dto.UserDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username, Model model) {
        UserDto found = userService.getUserByUsername(username);
        if (found == null) {
            return "404";
        }
        UserDto editUser = new UserDto(found.getId(), found.getUsername(), found.getEmail(), found.getRoles());
        model.addAttribute("editUser", editUser);
        model.addAttribute("user", found);
        return "profiles-detail";

    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable("userId") Integer userId,
            @Valid @ModelAttribute("editUser") UserDto newUser,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userService.getUserById(userId));
            model.addAttribute("validatedErrors", true);
            return "profiles-detail";
        }
        return MessageFormat.format("redirect:/profiles/{0}", newUser.getUsername());
    }

}

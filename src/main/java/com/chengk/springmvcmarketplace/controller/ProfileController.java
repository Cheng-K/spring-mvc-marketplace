package com.chengk.springmvcmarketplace.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.model.dto.UserDto;

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
        model.addAttribute("user", found);
        return "profiles-detail";

    }

}

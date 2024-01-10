package com.chengk.springmvcmarketplace.controller;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;
import com.chengk.springmvcmarketplace.model.dto.LoggedInUser;
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
            model.addAttribute("error", new HttpErrorDto(HttpStatus.NOT_FOUND, "User Profile Not Found",
                    MessageFormat.format("Unable to find user profile with username {0}", username)));
            return "http-error";
        }
        UserDto editUser = new UserDto(found.getId(), found.getUsername(), found.getEmail(), found.getRoles(),
                found.getProfilePicturePath());
        model.addAttribute("editUser", editUser);
        model.addAttribute("user", found);
        return "profiles-detail";

    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable("userId") Integer userId,
            @Valid @ModelAttribute("editUser") UserDto newUser,
            BindingResult bindingResult, Model model) {
        UserDto currentUser = userService.getUserById(userId);

        // check username changes
        if (!currentUser.getUsername().equals(newUser.getUsername())) {
            if (userService.usernameExists(newUser.getUsername())) {
                bindingResult.rejectValue("username", "username.exists",
                        "Username entered has already been used by another account. Please provide another one");
            } else {
                currentUser.setUsername(newUser.getUsername());
            }
        }

        // check email changes
        if (!currentUser.getEmail().equals(newUser.getEmail())) {
            if (userService.emailExists(newUser.getEmail())) {
                bindingResult.rejectValue("email", "email.exists",
                        "Email entered has already been used by another account. Please provide another one");
            } else {
                currentUser.setEmail(newUser.getEmail());
            }
        }

        // password checking
        if (!newUser.getPassword().isEmpty()) {
            if (newUser.getPassword().length() < 6) {
                bindingResult.rejectValue("password", "password.short", "Password must be at least 6 characters");
            } else if (newUser.getPassword().length() > 14) {
                bindingResult.rejectValue("password", "password.long", "Password must not exceed 14 characters");
            } else {
                currentUser.setPassword(newUser.getPassword());
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", currentUser);
            model.addAttribute("validatedErrors", true);
            return "profiles-detail";
        }

        currentUser.setUploadedProfilePicture(newUser.getUploadedProfilePicture());
        userService.editUser(currentUser);
        currentUser = userService.getUserById(userId);

        // dynamically update logged in user details
        LoggedInUser loggedInCurrent = new LoggedInUser(currentUser.getUsername(), "",
                currentUser.getGrantedAuthorities(), currentUser.getProfilePicturePath());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(loggedInCurrent,
                currentUser.getPassword(), currentUser.getGrantedAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return MessageFormat.format("redirect:/profiles/{0}", currentUser.getUsername());
    }

}

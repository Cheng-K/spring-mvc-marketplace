package com.chengk.springmvcmarketplace.domain;

import com.chengk.springmvcmarketplace.model.dto.UserDto;

public interface UserService {
    void addNewUser(UserDto userDto);

    UserDto getUserByUsername(String username);
}

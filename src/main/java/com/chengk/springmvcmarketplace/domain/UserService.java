package com.chengk.springmvcmarketplace.domain;

import com.chengk.springmvcmarketplace.model.dto.UserDto;

public interface UserService {
    void addNewUser(UserDto userDto);

    UserDto getUserByUsername(String username);

    UserDto getUserById(Integer id);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    void editUser(UserDto userDto);

    String getResetPasswordToken(UserDto userDto);

    UserDto getUserByUsernameAndEmail(String username, String email);
}

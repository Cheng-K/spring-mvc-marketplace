package com.chengk.springmvcmarketplace.domain;

import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private DtoConverter<Users, UserDto> userDtoConverter;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void addNewUser(UserDto userDto) {
        usersRepository.save(userDtoConverter.convertToEntity(userDto));
    }

}

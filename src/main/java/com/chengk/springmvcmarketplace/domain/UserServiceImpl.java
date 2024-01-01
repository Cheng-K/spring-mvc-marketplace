package com.chengk.springmvcmarketplace.domain;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private DtoConverter<Users, UserDto> userDtoConverter;

    public UserServiceImpl(UsersRepository usersRepository, DtoConverter<Users, UserDto> userDtoConverter) {
        this.usersRepository = usersRepository;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public void addNewUser(UserDto userDto) {
        usersRepository.save(userDtoConverter.convertToEntity(userDto));
    }

}

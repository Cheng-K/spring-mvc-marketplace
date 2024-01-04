package com.chengk.springmvcmarketplace.domain;

import java.util.Optional;

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

    @Override
    public UserDto getUserByUsername(String username) {
        Optional<Users> user = usersRepository.findByUsername(username);
        if (user.isEmpty())
            return null;
        return userDtoConverter.convertToDto(user.get());
    }

    @Override
    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public boolean usernameExists(String username) {
        return usersRepository.existsByUsername(username);
    }

    @Override
    public UserDto getUserById(Integer id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isEmpty())
            return null;
        return userDtoConverter.convertToDto(user.get());
    }

    @Override
    public void editUser(UserDto userDto) {
        Optional<Users> user = usersRepository.findById(userDto.getId());
        Users editedUser = userDtoConverter.convertToEntity(userDto);
        if (editedUser.getPassword() == null) {
            editedUser.setPassword(user.get().getPassword());
        }
        usersRepository.save(editedUser);
    }

}

package com.chengk.springmvcmarketplace.domain;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private StorageService storageService;
    private DtoConverter<Users, UserDto> userDtoConverter;

    public UserServiceImpl(UsersRepository usersRepository, StorageService storageService,
            DtoConverter<Users, UserDto> userDtoConverter) {
        this.usersRepository = usersRepository;
        this.storageService = storageService;
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

        // check picture
        String profilePicture = user.get().getProfilePicture();
        if (profilePicture != null && !profilePicture.isEmpty() && userDto.getUploadedProfilePicture() != null
                && !userDto.getUploadedProfilePicture().isEmpty()) {
            // delete the old picture
            try {
                storageService.deleteFile("./src/main/resources/images/profiles/" + profilePicture);
            } catch (Exception e) {
                System.err.println("Cannot delete file");
                e.printStackTrace();
                return;
            }
        }

        if (userDto.getUploadedProfilePicture() != null && !userDto.getUploadedProfilePicture().isEmpty()) {
            String fileName = storageService
                    .replaceFileNameWithUUID(userDto.getUploadedProfilePicture().getOriginalFilename());
            try {
                storageService.saveFile(userDto.getUploadedProfilePicture().getBytes(), fileName,
                        "./src/main/resources/images/profiles");
            } catch (IOException e) {
                System.err.println("Cannot read uploaded file");
                e.printStackTrace();
                return;
            }
            editedUser.setProfilePicture(fileName);
        }

        if (editedUser.getPassword() == null) {
            editedUser.setPassword(user.get().getPassword());
        }

        usersRepository.save(editedUser);
    }

}

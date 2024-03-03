package com.chengk.springmvcmarketplace.domain;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.chengk.springmvcmarketplace.model.dto.CartDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.entity.Cart;
import com.chengk.springmvcmarketplace.model.entity.CartProductRef;
import com.chengk.springmvcmarketplace.model.entity.Users;
import com.chengk.springmvcmarketplace.repository.CartRepository;
import com.chengk.springmvcmarketplace.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private CartRepository cartRepository;
    private StorageService storageService;
    private DtoConverter<Users, UserDto> userDtoConverter;
    private DtoConverter<Cart, CartDto> cartDtoConverter;
    private TokenService tokenService;

    public UserServiceImpl(UsersRepository usersRepository, CartRepository cartRepository,
            StorageService storageService,
            DtoConverter<Users, UserDto> userDtoConverter, DtoConverter<Cart, CartDto> cartDtoConverter,
            TokenService tokenService) {
        this.usersRepository = usersRepository;
        this.cartRepository = cartRepository;
        this.storageService = storageService;
        this.userDtoConverter = userDtoConverter;
        this.cartDtoConverter = cartDtoConverter;
        this.tokenService = tokenService;
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

    @Override
    public String getResetPasswordToken(UserDto resetUser) {
        String token = tokenService.generateTokenForUser(resetUser.getId().toString());
        Users saveUser = usersRepository.findById(resetUser.getId()).get();
        saveUser.setPasswordResetToken(token);
        usersRepository.save(saveUser);
        return token;
    }

    @Override
    public UserDto getUserByUsernameAndEmail(String username, String email) {
        Optional<Users> user = usersRepository.findByUsernameAndEmail(username, email);
        if (user.isEmpty())
            return null;
        return userDtoConverter.convertToDto(user.get());
    }

    @Override
    public boolean verifyPasswordResetToken(Integer id, String token) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent() && user.get().getPasswordResetToken().contentEquals(token)) {
            user.get().setPasswordResetToken(null);
            usersRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public CartDto getUserShoppingCart(Integer userId) {
        Optional<Cart> result = cartRepository.findById(userId);
        if (result.isEmpty())
            return null;
        return cartDtoConverter.convertToDto(result.get());
    }

    @Override
    public void addProductToShoppingCart(Integer userId, Integer productId) {
        Optional<Cart> result = cartRepository.findById(userId);
        if (result.isPresent()) {
            result.get().addProduct(productId);
            cartRepository.save(result.get());
        }
    }

    @Override
    public boolean productInUserCart(Integer userId, Integer productId) {
        Optional<Cart> result = cartRepository.findById(userId);
        if (result.isPresent()) {
            return result.get().getProducts().contains(new CartProductRef(productId));
        }
        return false;
    }

    @Override
    public void removeProductFromShoppingCart(Integer userId, Integer productId) {
        Optional<Cart> result = cartRepository.findById(userId);
        if (result.isPresent()) {
            result.get().getProducts().remove(new CartProductRef(productId));
            cartRepository.save(result.get());
        }
    }

}

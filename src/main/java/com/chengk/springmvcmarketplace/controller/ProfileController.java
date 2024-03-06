package com.chengk.springmvcmarketplace.controller;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chengk.springmvcmarketplace.domain.CategoryService;
import com.chengk.springmvcmarketplace.domain.ProductsService;
import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.CartDto;
import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;
import com.chengk.springmvcmarketplace.model.dto.LoggedInUser;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/profiles")
public class ProfileController {

    private UserService userService;
    private ProductsService productsService;
    private CategoryService categoryService;

    public ProfileController(UserService userService, ProductsService productsService,
            CategoryService categoryService) {
        this.userService = userService;
        this.productsService = productsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{username}")
    public String getProfile(@PathVariable("username") String username, Model model) {
        UserDto found = userService.getUserByUsername(username);
        if (found == null) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.NOT_FOUND, "User Profile Not Found",
                    MessageFormat.format("Unable to find user profile with username {0}", username)));
        }
        UserDto editUser = new UserDto(found.getId(), found.getUsername(), found.getEmail(), found.getRoles(),
                found.getProfilePicturePath());
        model.addAttribute("editUser", editUser);
        model.addAttribute("user", found);
        return "profile/profiles-detail";

    }

    @PostMapping("/{userId}/edit")
    public String editProfile(@PathVariable("userId") Integer userId,
            @Valid @ModelAttribute("editUser") UserDto newUser,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        UserDto currentUser = userService.getUserById(userId);

        if (!principal.getName().equals(currentUser.getUsername())) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.FORBIDDEN, "Cannot update account",
                    "You do not have the permission to update account that is not yours"));
        }

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
            return "profile/profiles-detail";
        }

        currentUser.setUploadedProfilePicture(newUser.getUploadedProfilePicture());
        userService.editUser(currentUser);
        currentUser = userService.getUserById(userId);

        // dynamically update logged in user details
        LoggedInUser loggedInCurrent = new LoggedInUser(currentUser.getUsername(), "",
                currentUser.getGrantedAuthorities(), currentUser.getProfilePicturePath(), currentUser.getId());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(loggedInCurrent,
                currentUser.getPassword(), currentUser.getGrantedAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Profile updated successfully");
        return MessageFormat.format("redirect:/profiles/{0}", currentUser.getUsername());
    }

    @GetMapping("/{userId}/products")
    public String getAllProducts(@RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "page", required = false) Integer page,
            @PathVariable("userId") Integer userId,
            Model model) {
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = MessageFormat.format("redirect:{0}",
                    ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page", 1).toUriString());
            return redirect;
        }
        int requestedPageInDb = page - 1;
        if (sortBy != null && sortBy.equals("latest")) {
            products = productsService.getAllUserProductsWithSort(Sort.by("listedOn").descending(), requestedPageInDb,
                    userId);
        } else if (sortBy != null && sortBy.equals("price")) {
            if (order != null && order.equals("desc")) {
                products = productsService.getAllUserProductsWithSort(Sort.by("price").descending(), requestedPageInDb,
                        userId);
            } else {
                products = productsService.getAllUserProductsWithSort(Sort.by("price").ascending(), requestedPageInDb,
                        userId);
            }
        } else {
            products = productsService.getAllUserProductsWithPagination(requestedPageInDb, userId);
        }

        model.addAttribute("products", products);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        return "profile/profiles-products-list";
    }

    @GetMapping("/{userId}/products/search")
    public String searchProducts(@RequestParam("query") String query,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "page", required = false) Integer page,
            @PathVariable("userId") Integer userId,
            Model model) {
        if (query == null || query.isBlank()) {
            return MessageFormat.format("redirect:{0}", MvcUriComponentsBuilder
                    .fromMethodName(this.getClass(), "getAllProducts", sortBy, order, 1, userId, model)
                    .build().toUriString());
        }
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = MessageFormat.format("redirect:{0}",
                    ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page", 1).toUriString());
            return redirect;
        }
        int requestedPageInDb = page - 1;
        if (sortBy != null && sortBy.equals("latest")) {
            products = productsService.getUserProductsByQueryOrderByLatest(query,
                    requestedPageInDb, userId);
            System.out.println(products);
        } else if (sortBy != null && sortBy.equals("price")) {
            if (order != null && order.equals("desc")) {
                products = productsService.getUserProductsByQueryOrderByPrice(query,
                        requestedPageInDb, userId, false);
            } else {
                products = productsService.getUserProductsByQueryOrderByPrice(query,
                        requestedPageInDb, userId, true);
            }
        } else {
            products = productsService.getUserProductsByQuery(query, requestedPageInDb, userId);
        }

        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        model.addAttribute("products", products);
        return "profile/profiles-products-list";
    }

    @GetMapping("/{userId}/products/categories/{categoryId}")
    public String getProductsByCategory(@PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "categoryId") Integer categoryId,
            @RequestParam(name = "page", required = false) Integer page, Model model) {
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = MessageFormat.format("redirect:{0}",
                    ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page", 1).toUriString());
            return redirect;
        }
        int requestedPageInDb = page - 1;

        products = productsService.getUserProductsByCategoryId(categoryId, requestedPageInDb, userId);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("products", products);
        return "profile/profiles-products-list";
    }

    @GetMapping("/cart")
    public String getShoppingCart(@AuthenticationPrincipal LoggedInUser principal, Model model) {
        CartDto carts = userService.getUserShoppingCart(principal.getId());
        double totalPrice = carts.getProducts().stream().map(product -> product.getPrice()).reduce(0.0,
                (currentTotal, product) -> currentTotal + product);
        model.addAttribute("products", carts.getProducts());
        model.addAttribute("totalCheckoutPrice", (Double) totalPrice);
        return "profile/profiles-cart";
    }

    @PostMapping("/cart")
    public String postProductShoppingCart(@RequestParam("productId") Integer productId,
            @RequestParam("redirect") String link,
            @AuthenticationPrincipal LoggedInUser principal, RedirectAttributes redirectAttributes) {
        System.out.println("hi");
        userService.addProductToShoppingCart(principal.getId(), productId);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Product added to cart successfully");
        return "redirect:" + link;
    }

    @DeleteMapping("/cart")
    public String deleteProductShoppingCart(@RequestParam("productId") Integer productId,
            @RequestParam("redirect") String link,
            @AuthenticationPrincipal LoggedInUser principal, RedirectAttributes redirectAttributes) {
        userService.removeProductFromShoppingCart(principal.getId(), productId);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Product removed from cart successfully");
        return "redirect:" + link;
    }

}

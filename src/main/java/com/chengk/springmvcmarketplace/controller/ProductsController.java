package com.chengk.springmvcmarketplace.controller;

import java.security.Principal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chengk.springmvcmarketplace.domain.CategoryService;
import com.chengk.springmvcmarketplace.domain.ProductsService;
import com.chengk.springmvcmarketplace.domain.UserService;
import com.chengk.springmvcmarketplace.domain.exceptions.AppResponseException;
import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.HttpErrorDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.dto.UserDto;
import com.chengk.springmvcmarketplace.model.value_objects.Condition;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;
    private CategoryService categoryService;
    private UserService userService;

    public ProductsController(ProductsService productsService, CategoryService categoryService,
            UserService userService) {
        this.productsService = productsService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllProducts(@RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "page", required = false) Integer page,
            Model model) {
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = "redirect:/products?page=1";
            if (sortBy != null) {
                redirect += MessageFormat.format("&sortBy={0}", sortBy);
            }
            if (order != null) {
                redirect += MessageFormat.format("&order={0}", order);
            }
            return redirect;
        }
        int requestedPageInDb = page - 1;
        if (sortBy != null && sortBy.equals("latest")) {
            products = productsService.getAllProductsWithSort(Sort.by("listedOn").descending(), requestedPageInDb);
        } else if (sortBy != null && sortBy.equals("price")) {
            if (order != null && order.equals("desc")) {
                products = productsService.getAllProductsWithSort(Sort.by("price").descending(), requestedPageInDb);
            } else {
                products = productsService.getAllProductsWithSort(Sort.by("price").ascending(), requestedPageInDb);
            }
        } else {
            products = productsService.getAllProductsWithPagination(requestedPageInDb);
        }

        model.addAttribute("products", products);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        return "products-list";
    }

    @GetMapping("/add")
    public String getAddProductsForm(Model model) {
        ProductDto newProduct = new ProductDto();
        newProduct.setCondition(Condition.USED);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        model.addAttribute("newProduct", newProduct);
        return "products-add";
    }

    @PostMapping("/add")
    public String postAddProductsForm(@ModelAttribute("newProduct") @Valid ProductDto productDto,
            BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (productDto.getImageFile() == null || productDto.getImageFile().isEmpty()) {
            result.rejectValue("imageFile", "no.image.uploaded", "Product image must be uploaded.");
        }
        if (result.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAllCategories();
            model.addAttribute("availableCategories", categories);
            return "products-add";
        }
        UserDto seller = userService.getUserByUsername(principal.getName());
        productDto.setListedOn(LocalDateTime.now());
        productDto.setSeller(seller);
        productsService.addNewProduct(productDto);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Product added successfully");
        return "redirect:/products";
    }

    @GetMapping("/{productId}")
    public String getProductDetails(@PathVariable("productId") Integer productId, Model model) {
        ProductDto productDto = productsService.getProductById(productId);
        if (productDto == null) {
            throw new AppResponseException(HttpErrorDto.createProductNotFoundError());
        }
        model.addAttribute("product", productDto);
        return "products-detail";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Integer productId, Principal principal, Model model,
            RedirectAttributes redirectAttributes) {
        ProductDto productDto = productsService.getProductById(productId);
        if (!productDto.getSeller().getUsername().equals(principal.getName())) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.UNAUTHORIZED, "Cannot delete product",
                    "Unexpected error encountered when deleting the product"));
        }

        productsService.removeProduct(productId);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Product deleted successfully");
        return "redirect:/products";
    }

    @GetMapping("/{productId}/edit")
    public String getEditProductForm(@PathVariable("productId") Integer productId, Model model) {
        ProductDto productDto = productsService.getProductById(productId);
        if (productDto == null) {
            throw new AppResponseException(HttpErrorDto.createProductNotFoundError());
        }
        model.addAttribute("product", productDto);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        return "products-edit";
    }

    @PutMapping("/{productId}")
    public String updateProductDetails(@PathVariable("productId") Integer productId,
            @ModelAttribute("product") @Valid ProductDto productDto, BindingResult bindingResult, Model model,
            Principal principal, RedirectAttributes redirectAttributes) {
        if (!productDto.getSeller().getUsername().equals(principal.getName())) {
            throw new AppResponseException(new HttpErrorDto(HttpStatus.UNAUTHORIZED, "Cannot update product",
                    "Unexpected error encountered when updating the product"));
        }
        if (bindingResult.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAllCategories();
            model.addAttribute("availableCategories", categories);
            return "products-edit";
        }
        productDto.setId(productId);
        productsService.editProduct(productDto);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Product updated successfully");
        return String.format("redirect:/products/%d", productId);
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "page", required = false) Integer page,
            Model model) {
        if (query == null || query.isBlank()) {
            return "redirect:/products?page=1";
        }
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = MessageFormat.format("redirect:/products/search?page=1&query={0}", query);
            if (sortBy != null) {
                redirect += MessageFormat.format("&sortBy={0}", sortBy);
            }
            if (order != null) {
                redirect += MessageFormat.format("&order={0}", order);
            }
            return redirect;
        }
        int requestedPageInDb = page - 1;
        if (sortBy != null && sortBy.equals("latest")) {
            products = productsService.getProductsByQueryWithSort(query, Sort.by("listedOn").descending(),
                    requestedPageInDb);
        } else if (sortBy != null && sortBy.equals("price")) {
            if (order != null && order.equals("desc")) {
                products = productsService.getProductsByQueryWithSort(query, Sort.by("price").descending(),
                        requestedPageInDb);
            } else {
                products = productsService.getProductsByQueryWithSort(query, Sort.by("price").ascending(),
                        requestedPageInDb);
            }
        } else {
            products = productsService.getProductsByQuery(query, requestedPageInDb);
        }

        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        model.addAttribute("products", products);
        return "products-list";
    }

    @GetMapping("/categories/{categoryId}")
    public String getProductsByCategory(@PathVariable("categoryId") Integer categoryId,
            @RequestParam(name = "page", required = false) Integer page, Model model) {
        Slice<ProductDto> products = null;
        if (page == null || page <= 0) {
            String redirect = MessageFormat.format("redirect:/products/categories/{0}?page=1", categoryId);
            return redirect;
        }
        int requestedPageInDb = page - 1;

        products = productsService.getProductsByCategoryId(categoryId, requestedPageInDb);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("products", products);
        return "products-list";
    }

}

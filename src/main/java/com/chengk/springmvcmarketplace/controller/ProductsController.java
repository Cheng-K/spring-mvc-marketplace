package com.chengk.springmvcmarketplace.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.CategoryService;
import com.chengk.springmvcmarketplace.domain.ProductsService;
import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.value_objects.Condition;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;
    private CategoryService categoryService;

    public ProductsController(ProductsService productsService, CategoryService categoryService) {
        this.productsService = productsService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductDto> products = productsService.getAllProducts();
        model.addAttribute("products", products);
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
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAllCategories();
            model.addAttribute("availableCategories", categories);
            return "products-add";
        }
        productDto.setListedOn(Timestamp.valueOf(LocalDateTime.now()));
        productsService.addNewProduct(productDto);
        return "redirect:/products";
    }

}

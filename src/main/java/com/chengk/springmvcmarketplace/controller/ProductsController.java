package com.chengk.springmvcmarketplace.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.ProductsService;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductDto> products = productsService.getAllProducts();
        model.addAttribute("products", products);
        return "products-list";
    }

    @GetMapping("/add")
    public String getAddProductsForm() {
        return "products-add";
    }

}

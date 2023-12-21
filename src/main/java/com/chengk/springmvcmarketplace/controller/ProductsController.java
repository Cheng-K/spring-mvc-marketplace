package com.chengk.springmvcmarketplace.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chengk.springmvcmarketplace.domain.CategoryService;
import com.chengk.springmvcmarketplace.domain.ProductsService;
import com.chengk.springmvcmarketplace.model.dto.CategoryDto;
import com.chengk.springmvcmarketplace.model.dto.ProductDto;
import com.chengk.springmvcmarketplace.model.value_objects.Condition;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

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
        if (productDto.getImageFile() == null || productDto.getImageFile().isEmpty()) {
            result.rejectValue("imageFile", "no.image.uploaded", "Product image must be uploaded.");
        }
        if (result.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAllCategories();
            model.addAttribute("availableCategories", categories);
            return "products-add";
        }
        productDto.setListedOn(LocalDateTime.now());
        productsService.addNewProduct(productDto);
        return "redirect:/products";
    }

    @GetMapping("/{productId}")
    public String getProductDetails(@PathVariable("productId") Integer productId, Model model) {
        ProductDto productDto = productsService.getProductById(productId);
        if (productDto == null)
            return "404";
        model.addAttribute("product", productDto);
        return "products-detail";
    }

    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable("productId") Integer productId) {
        productsService.removeProduct(productId);
        return "redirect:/products";
    }

    @GetMapping("/{productId}/edit")
    public String getEditProductForm(@PathVariable("productId") Integer productId, Model model) {
        ProductDto productDto = productsService.getProductById(productId);
        if (productDto == null)
            return "404";
        model.addAttribute("product", productDto);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("availableCategories", categories);
        return "products-edit";
    }

    @PutMapping("/{productId}")
    public String updateProductDetails(@PathVariable("productId") Integer productId,
            @ModelAttribute("product") @Valid ProductDto productDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<CategoryDto> categories = categoryService.getAllCategories();
            model.addAttribute("availableCategories", categories);
            return "products-edit";
        }
        productDto.setId(productId);
        productsService.editProduct(productDto);
        return String.format("redirect:/products/%d", productId);
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        model.addAttribute("products", productsService.getProductsByQuery(query));
        return "products-list";
    }

}

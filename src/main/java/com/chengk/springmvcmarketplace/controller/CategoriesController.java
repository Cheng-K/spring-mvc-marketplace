package com.chengk.springmvcmarketplace.controller;

import java.time.LocalDateTime;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chengk.springmvcmarketplace.domain.CategoryService;
import com.chengk.springmvcmarketplace.model.dto.CategoryDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("newCategory", new CategoryDto());
        model.addAttribute("submittedCategory", new CategoryDto());
        return "categories-list";
    }

    @PostMapping
    public String postNewCategory(@Valid @ModelAttribute("submittedCategory") CategoryDto categoryDto,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        if (!categoryDto.getTitle().isEmpty() && categoryService.doesCategoryExists(categoryDto.getTitle())) {
            result.rejectValue("title", "duplicate.category", "Category already exist.");
        }
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newCategory", new CategoryDto());
            model.addAttribute("hasErrors", "new");
            return "categories-list";
        }
        categoryDto.setCreatedOn(LocalDateTime.now());
        categoryDto.setLastUpdated(LocalDateTime.now());
        categoryService.addNewCategory(categoryDto);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "New category added successfully");
        return "redirect:/categories";
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer categoryId,
            RedirectAttributes redirectAttributes) {
        categoryService.removeCategory(categoryId);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Category deleted successfully");
        return "redirect:/categories";
    }

    @PutMapping("/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") Integer categoryId,
            @Valid @ModelAttribute("submittedCategory") CategoryDto categoryDto, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {

        if (!categoryDto.getTitle().isEmpty() && categoryService.doesCategoryExists(categoryDto.getTitle())) {
            result.rejectValue("title", "duplicate.category", "Category already exist.");
        }
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("newCategory", new CategoryDto());
            model.addAttribute("hasErrors", categoryId);
            return "categories-list";
        }
        categoryDto.setId(categoryId);
        categoryService.editCategory(categoryDto);
        redirectAttributes.addFlashAttribute("postRedirectMessage", "Category updated successfully");
        return "redirect:/categories";
    }

}

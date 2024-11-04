package com.lew663.blog.domain.category.controller;

import com.lew663.blog.domain.category.dto.CategoryForm;
import com.lew663.blog.domain.category.dto.CategoryInfo;
import com.lew663.blog.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/category")
  public String categoryForm(Model model) {
    model.addAttribute("categoryForm", new CategoryForm());
    List<CategoryInfo> parentCategories = categoryService.findAllParentCategories();
    model.addAttribute("parentCategories", parentCategories);
    return "admin/category/categoryForm";
  }
  @PostMapping("/category")
  public String createCategory(@Validated @ModelAttribute CategoryForm categoryForm) {
    categoryService.createCategory(categoryForm);
    return "redirect:/";
  }
}
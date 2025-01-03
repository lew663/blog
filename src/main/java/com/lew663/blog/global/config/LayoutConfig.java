package com.lew663.blog.global.config;

import com.lew663.blog.domain.category.dto.CategoryInfo;
import com.lew663.blog.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LayoutConfig {

  private final CategoryService categoryService;

  public void AddLayoutTo(Model model) {
    List<CategoryInfo> categories = categoryService.findAllCategories();
    model.addAttribute("categories", categories);
  }
}
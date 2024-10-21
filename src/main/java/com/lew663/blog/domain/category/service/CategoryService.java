package com.lew663.blog.domain.category.service;

import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.dto.CategoryForm;
import com.lew663.blog.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> findAllParentCategories() {
    return categoryRepository.findAllByParentIsNull();
  }

  public List<Category> findAllCategories() {
    return categoryRepository.findAll();
  }

  public void createCategory(CategoryForm categoryForm) {
    Category category = new Category(categoryForm.getTitle());
    if (categoryForm.getParentId() != null) {
      Category parent = categoryRepository.findById(categoryForm.getParentId())
          .orElseThrow(() -> new IllegalArgumentException("해당 상위 카테고리가 존재하지 않습니다."));
      category.setParent(parent);
    }
    categoryRepository.save(category);
  }
}
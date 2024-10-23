package com.lew663.blog.domain.category.service;

import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.dto.CategoryForm;
import com.lew663.blog.domain.category.dto.CategoryInfo;
import com.lew663.blog.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional(readOnly = true)
  public List<CategoryInfo> findAllParentCategories() {
    return categoryRepository.findAllByParentIsNull().stream()
        .map(CategoryInfo::from)
        .collect(Collectors.toList());
  }
  @Transactional(readOnly = true)
  public List<CategoryInfo> findAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    categories.forEach(category -> Hibernate.initialize(category.getChild()));
    return categories.stream()
        .map(CategoryInfo::from)
        .collect(Collectors.toList());
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
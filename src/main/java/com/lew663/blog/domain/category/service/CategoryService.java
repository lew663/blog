package com.lew663.blog.domain.category.service;

import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.dto.CategoryForm;
import com.lew663.blog.domain.category.dto.CategoryInfo;
import com.lew663.blog.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  // 모든 카테고리 조회
  public List<CategoryInfo> getAllCategories() {
    return categoryRepository.findAll().stream()
        .map(CategoryInfo::from)
        .collect(Collectors.toList());
  }

  // 카테고리 생성
  public Category createCategory(CategoryForm categoryForm) {
    Category category = new Category(categoryForm.getTitle());
    return categoryRepository.save(category);
  }

  // 카테고리 수정
  public Category updateCategory(Long id, CategoryForm categoryForm) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

    category.changeTitle(categoryForm.getTitle());
    return categoryRepository.save(category);
  }

  // 카테고리 삭제
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }

  // 카테고리 상세 조회 (ID로 조회)
  public Optional<CategoryInfo> getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .map(CategoryInfo::from);
  }
}
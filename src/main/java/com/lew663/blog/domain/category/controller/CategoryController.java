package com.lew663.blog.domain.category.controller;

import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.dto.CategoryForm;
import com.lew663.blog.domain.category.dto.CategoryInfo;
import com.lew663.blog.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  // 모든 카테고리 조회
  @GetMapping
  public ResponseEntity<List<CategoryInfo>> getAllCategories() {
    List<CategoryInfo> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  // 특정 카테고리 조회
  @GetMapping("/{id}")
  public ResponseEntity<CategoryInfo> getCategoryById(@PathVariable Long id) {
    return categoryService.getCategoryById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 카테고리 생성
  @PostMapping
  public ResponseEntity<CategoryInfo> createCategory(@Validated @RequestBody CategoryForm categoryForm) {
    Category category = categoryService.createCategory(categoryForm);
    return ResponseEntity.ok(CategoryInfo.from(category));
  }

  // 카테고리 수정
  @PutMapping("/{id}")
  public ResponseEntity<CategoryInfo> updateCategory(@PathVariable Long id, @Validated @RequestBody CategoryForm categoryForm) {
    Category updatedCategory = categoryService.updateCategory(id, categoryForm);
    return ResponseEntity.ok(CategoryInfo.from(updatedCategory));
  }

  // 카테고리 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
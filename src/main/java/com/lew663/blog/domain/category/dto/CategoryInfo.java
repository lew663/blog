package com.lew663.blog.domain.category.dto;

import com.lew663.blog.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CategoryInfo {
  private Long id;
  private String title;
  private Long parentId;
  private List<CategoryInfo> childCategories;

  public static CategoryInfo from(Category category) {
    List<CategoryInfo> childCategoryInfos = category.getChild().stream()
        .map(CategoryInfo::from)
        .collect(Collectors.toList());
    return new CategoryInfo(
        category.getId(),
        category.getTitle(),
        category.getParent() != null ? category.getParent().getId() : null,
        childCategoryInfos
    );
  }
}
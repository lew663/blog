package com.lew663.blog.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {

  @NotBlank(message = "카테고리 이름을 작성해주세요.")
  @Size(min = 1, max = 20, message = "카테고리 이름은 20자 이내로 작성해주세요.")
  private String title;

  private Long parentId;
}

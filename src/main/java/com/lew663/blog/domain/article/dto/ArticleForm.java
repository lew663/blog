package com.lew663.blog.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleForm {
  private String title;
  private String content;
  private List<String> tags;
  private Long categoryId;
}
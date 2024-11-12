package com.lew663.blog.domain.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ArticleSummaryInfo {
  private Long id;
  private String title;
  private LocalDate createdDate;

  public ArticleSummaryInfo(Long id, String title, LocalDate createdDate) {
    this.id = id;
    this.title = title;
    this.createdDate = createdDate;
  }
}

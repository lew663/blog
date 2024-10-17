package com.lew663.blog.domain.article.dto;

import com.lew663.blog.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ArticleInfo {
  private Long id;
  private String title;
  private String content;
  private String email;
  private Long hits;
  private List<String> tags;

  public static ArticleInfo from(Article article) {
    List<String> tagNames = article.getArticleTagLists().stream()
        .map(articleTagList -> articleTagList.getTags().getName())
        .collect(Collectors.toList());

    return new ArticleInfo(
        article.getId(),
        article.getTitle(),
        article.getContent(),
        article.getMember().getEmail(),
        article.getHit(),
        tagNames
    );
  }
}
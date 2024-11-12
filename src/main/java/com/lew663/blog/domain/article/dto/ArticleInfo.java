package com.lew663.blog.domain.article.dto;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.comment.dto.CommentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ArticleInfo {

  private Long id;
  private String title;
  private String content;
  private String email;
  private long viewCount;
  private List<String> tags;
  private LocalDate createdDate;

  public static ArticleInfo from(Article article) {
    return new ArticleInfo(
        article.getId(),
        article.getTitle(),
        article.getContent(),
        article.getMember().getEmail(),
        getSafeViewCount(article),
        getTags(article),
        article.getCreatedDate()
        );
  }

  private static long getSafeViewCount(Article article) {
    return article.getViewCount() != null ? article.getViewCount() : 0;
  }

  private static List<String> getTags(Article article) {
    return article.getArticleTagLists().stream()
        .map(articleTagList -> articleTagList.getTags().getName())
        .collect(Collectors.toList());
  }
}


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
  private Long categoryId;
  private LocalDate createdDate;
  private List<CommentInfo> comments;

  public static ArticleInfo from(Article article) {
    List<String> tagNames = article.getArticleTagLists().stream()
        .map(articleTagList -> articleTagList.getTags().getName())
        .collect(Collectors.toList());

    List<CommentInfo> commentInfos = article.getParentCommentList().stream()
        .map(CommentInfo::from)
        .collect(Collectors.toList());

    return new ArticleInfo(
        article.getId(),
        article.getTitle(),
        article.getContent(),
        article.getMember().getEmail(),
        article.getViewCount() != null ? article.getViewCount().getViewCount() : 0,
        tagNames,
        article.getCategory() != null ? article.getCategory().getId() : null,
        article.getCreatedDate(),
        commentInfos
    );
  }
}
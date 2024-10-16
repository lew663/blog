package com.lew663.blog.domain.comment.dto;

import com.lew663.blog.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentInfo {
  private Long id;
  private String content;
  private String memberEmail;
  private Long articleId;

  public static CommentInfo from(Comment comment) {
    return new CommentInfo(
        comment.getId(),
        comment.getContent(),
        comment.getMember().getEmail(),
        comment.getArticle().getId()
    );
  }
}
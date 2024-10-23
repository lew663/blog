package com.lew663.blog.domain.comment.dto;

import com.lew663.blog.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CommentInfo {
  private Long id;
  private String content;
  private String email;
  private Long articleId;
  private List<CommentInfo> childComments;
  private LocalDate createdDate;

  public static CommentInfo from(Comment comment) {
    List<CommentInfo> childCommentInfos = comment.getChild().stream()
        .map(CommentInfo::from)
        .collect(Collectors.toList());

    return new CommentInfo(
        comment.getId(),
        comment.getContent(),
        comment.getMember().getEmail(),
        comment.getArticle().getId(),
        childCommentInfos,
        comment.getCreatedDate()
    );
  }
}
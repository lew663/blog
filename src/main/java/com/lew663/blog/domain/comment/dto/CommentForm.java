package com.lew663.blog.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {

  @NotBlank(message = "댓글 내용을 작성해주세요")
  @Size(min = 1,max = 250, message = "댓글은 250자 이내로 작성해주세요")
  private String content;
}

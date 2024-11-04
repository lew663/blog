package com.lew663.blog.domain.comment.controller;

import com.lew663.blog.domain.comment.dto.CommentForm;
import com.lew663.blog.domain.comment.dto.CommentInfo;
import com.lew663.blog.domain.comment.service.CommentService;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 생성
  @PostMapping
  public ResponseEntity<CommentInfo> createComment(@RequestParam Long articleId,
                                                   @RequestParam(required = false) Long parentId,
                                                   @Valid @RequestBody CommentForm commentForm,
                                                   @AuthenticationPrincipal PrincipalDetail principalDetail) {
    CommentInfo commentInfo = commentService.createComment(articleId, principalDetail.getName(), commentForm, parentId);
    return new ResponseEntity<>(commentInfo, HttpStatus.CREATED);
  }
}

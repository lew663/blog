package com.lew663.blog.domain.comment.controller;

import com.lew663.blog.domain.comment.dto.CommentForm;
import com.lew663.blog.domain.comment.dto.CommentInfo;
import com.lew663.blog.domain.comment.service.CommentService;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 생성
  @PostMapping("/article/view")
  public String createComment(@RequestParam("articleId") Long articleId,
                              @RequestParam(name = "parentId", required = false) Long parentId,
                              @Valid @ModelAttribute CommentForm commentForm,
                              @AuthenticationPrincipal PrincipalDetail principalDetail) {
    CommentInfo commentInfo = commentService.createComment(articleId, principalDetail.getName(), commentForm, parentId);
    return "redirect:/article/view?articleId=" + articleId;
  }
}

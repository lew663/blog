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

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  // 댓글 생성
  @PostMapping
  public ResponseEntity<CommentInfo> createComment(@RequestParam Long articleId,
                                                   @Valid @RequestBody CommentForm commentForm,
                                                   @RequestParam(required = false) Long parentId,
                                                   @AuthenticationPrincipal PrincipalDetail principalDetail) {
    CommentInfo commentInfo = commentService.createComment(articleId, principalDetail.getName(), commentForm, parentId);
    return new ResponseEntity<>(commentInfo, HttpStatus.CREATED);
  }

  // 댓글 수정
  @PutMapping
  public ResponseEntity<Void> updateComment(@RequestParam Long commentId,
                                            @Valid @RequestBody CommentForm commentForm,
                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
    commentService.updateComment(commentId, principalDetail.getName(), commentForm);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 댓글 삭제
  @DeleteMapping
  public ResponseEntity<Void> deleteComment(@RequestParam Long commentId,
                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
    commentService.deleteComment(commentId, principalDetail.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // 특정 게시글의 모든 댓글 조회
  @GetMapping("/article")
  public ResponseEntity<List<CommentInfo>> getCommentsByArticleId(@RequestParam Long articleId) {
    List<CommentInfo> commentInfos = commentService.getCommentsByArticleId(articleId);
    return new ResponseEntity<>(commentInfos, HttpStatus.OK);
  }
}

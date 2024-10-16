package com.lew663.blog.domain.article.controller;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.service.ArticleService;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

  private final ArticleService articleService;

  // 게시글 생성
  @PostMapping
  public ResponseEntity<Article> createArticle(@RequestBody ArticleForm articleForm,
                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
    Article createdArticle = articleService.createArticle(articleForm, principalDetail.getName());
    return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
  }

  // 게시글 수정
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleForm articleForm,
                                               @AuthenticationPrincipal PrincipalDetail principalDetail) {
    articleService.updateArticle(id, articleForm, principalDetail.getName());
    return new ResponseEntity<>( HttpStatus.OK);
  }

  // 게시글 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id,
                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
    articleService.deleteArticle(id, principalDetail.getName());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // 게시글 조회 (특정 게시물) - 인증 정보 필요 없음
  @GetMapping("/{id}")
  public ResponseEntity<ArticleInfo> getArticle(@PathVariable Long id) {
    ArticleInfo articleInfo = articleService.getArticleById(id);
    return new ResponseEntity<>(articleInfo, HttpStatus.OK);
  }

  // 게시글 조회 (전체 게시글) - 인증 정보 필요 없음
  @GetMapping
  public ResponseEntity<List<ArticleInfo>> getAllArticles() {
    List<ArticleInfo> articles = articleService.getAllArticles();
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }
}

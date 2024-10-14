package com.lew663.blog.domain.Article.controller;

import com.lew663.blog.domain.Article.Article;
import com.lew663.blog.domain.Article.dto.ArticleDto;
import com.lew663.blog.domain.Article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  // 게시글 생성
  @PostMapping
  public ResponseEntity<Article> createArticle(@RequestBody ArticleDto articleDto) {
    Article createdArticle = articleService.createArticle(articleDto);
    return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
  }

  // 게시글 수정
  @PutMapping("/{id}")
  public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto) {
    Article updatedArticle = articleService.updateArticle(id, articleDto);
    return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
  }

  // 게시글 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    articleService.deleteArticle(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // 게시글 조회 (특정 게시물)
  @GetMapping("/{id}")
  public ResponseEntity<Article> getArticle(@PathVariable Long id) {
    Article article = articleService.getArticleById(id);
    return new ResponseEntity<>(article, HttpStatus.OK);
  }

  // 게시글 조회 (전체 게시글)
  @GetMapping
  public ResponseEntity<List<Article>> getAllArticles() {
    List<Article> articles = articleService.getAllArticles();
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }
}

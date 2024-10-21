package com.lew663.blog.domain.article.controller;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.service.ArticleService;
import com.lew663.blog.domain.category.service.CategoryService;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import com.lew663.blog.global.config.LayoutConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {

  private final CategoryService categoryService;
  private final ArticleService articleService;
  private final LayoutConfig layoutConfig;

  // 게시글 생성
  @GetMapping("/article/write")
  public String getArticleWriteForm(Model model) {
    layoutConfig.AddLayoutTo(model);
    model.addAttribute("articleForm", new ArticleForm());
    model.addAttribute("categories", categoryService.findAllCategories());
    return "article/articleForm";
  }

  @PostMapping("/article/write")
  public String createArticle(@ModelAttribute ArticleForm articleForm,
                              @AuthenticationPrincipal PrincipalDetail principalDetail) {
    ArticleInfo articleInfo = articleService.createArticle(articleForm, principalDetail.getName());
    return "redirect:/";
  }

//  // 게시글 수정
//  @PutMapping("/{id}")
//  public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleForm articleForm,
//                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
//    articleService.updateArticle(id, articleForm, principalDetail.getName());
//    return new ResponseEntity<>(HttpStatus.OK);
//  }
//
//  // 게시글 삭제
//  @DeleteMapping("/{id}")
//  public ResponseEntity<Void> deleteArticle(@PathVariable Long id,
//                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
//    articleService.deleteArticle(id, principalDetail.getName());
//    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//  }
//
//  // 게시글 조회 (특정 게시물) - 인증 정보 필요 없음
//  @GetMapping("/{id}")
//  public ResponseEntity<ArticleInfo> getArticle(@PathVariable Long id) {
//    ArticleInfo articleInfo = articleService.getArticleById(id);
//    return new ResponseEntity<>(articleInfo, HttpStatus.OK);
//  }
//
//  // 게시글 조회 (전체 게시글) - 인증 정보 필요 없음
//  @GetMapping
//  public ResponseEntity<List<ArticleInfo>> getAllArticles() {
//    List<ArticleInfo> articles = articleService.getAllArticles();
//    return new ResponseEntity<>(articles, HttpStatus.OK);
//  }

}

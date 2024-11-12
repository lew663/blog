package com.lew663.blog.domain.article.controller;

import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.dto.ArticleSummaryInfo;
import com.lew663.blog.domain.article.service.ArticleService;
import com.lew663.blog.domain.comment.dto.CommentForm;
import com.lew663.blog.domain.comment.dto.CommentInfo;
import com.lew663.blog.domain.comment.service.CommentService;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import com.lew663.blog.global.config.LayoutConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

  private final ArticleService articleService;
  private final CommentService commentService;
  private final LayoutConfig layoutConfig;

  /**
   * 게시글 조회
   */
  // 전체글 조회
  @GetMapping("/article/list")
  public String readArticleList(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {
    layoutConfig.AddLayoutTo(model);
    Page<ArticleSummaryInfo> articlePage = articleService.getAllArticles(pageable);
    model.addAttribute("articles", articlePage.getContent());
    model.addAttribute("totalPages", articlePage.getTotalPages());
    model.addAttribute("currentPage", articlePage.getNumber());
    model.addAttribute("hasNext", articlePage.hasNext());
    model.addAttribute("hasPrevious", articlePage.hasPrevious());
    return "article/articleList";
  }

  // 카테고리로 조회
  @GetMapping("/article/list/category")
  public String readArticleListByCategory(@RequestParam String categoryTitle,
                                          @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                          Model model) {
    layoutConfig.AddLayoutTo(model);
    Page<ArticleSummaryInfo> articlePage = articleService.getArticleByCategoryTitle(categoryTitle, pageable);
    model.addAttribute("categoryTitle", categoryTitle);
    model.addAttribute("articles", articlePage.getContent());
    model.addAttribute("totalPages", articlePage.getTotalPages());
    model.addAttribute("currentPage", articlePage.getNumber());
    model.addAttribute("hasNext", articlePage.hasNext());
    model.addAttribute("hasPrevious", articlePage.hasPrevious());
    return "article/articleListByCategory";
  }

  // id로 조회
  @GetMapping("/article/view")
  public String readArticleListById(@RequestParam Long articleId,
                                    Model model) {
    layoutConfig.AddLayoutTo(model);
    ArticleInfo articleInfo = articleService.getArticleById(articleId);
    List<CommentInfo> comments = commentService.getCommentsByArticleId(articleId);
    model.addAttribute("article", articleInfo);
    model.addAttribute("comments", comments);
    model.addAttribute("commentForm", new CommentForm());
    return "article/articleView";
  }

  // 검색어 별로 게시글 조회
  @GetMapping("/article/list/search")
  public String readArticleListByKeyword(@RequestParam String keyword,
                                         @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                         Model model) {
    if (keyword == null || keyword.trim().isEmpty()) {
      keyword = "";
    }
    layoutConfig.AddLayoutTo(model);
    Page<ArticleSummaryInfo> articlePage = articleService.searchArticles(keyword, pageable);
    model.addAttribute("articles", articlePage.getContent());
    model.addAttribute("totalPages", articlePage.getTotalPages());
    model.addAttribute("currentPage", articlePage.getNumber());
    model.addAttribute("hasNext", articlePage.hasNext());
    model.addAttribute("hasPrevious", articlePage.hasPrevious());
    model.addAttribute("keyword", keyword);
    return "article/articleListByKeyword";
  }

  /**
   * 게시글 작성
   */
  @GetMapping("/article/write")
  public String getArticleWriteForm(Model model) {
    layoutConfig.AddLayoutTo(model);
    model.addAttribute("articleForm", new ArticleForm());
    return "admin/article/articleForm";
  }

  @PostMapping("/article/write")
  public String createArticle(@ModelAttribute ArticleForm articleForm,
                              @AuthenticationPrincipal PrincipalDetail principalDetail) {
    ArticleInfo articleInfo = articleService.createArticle(articleForm, principalDetail.getName());
    Long articleId = articleInfo.getId();
    return "redirect:/article/view?articleId=" + articleId;
  }

  /**
   * 게시글 수정
   */
  @GetMapping("/article/edit")
  public String updateArticleForm(@RequestParam Long articleId,
                                  Model model) {
    layoutConfig.AddLayoutTo(model);
    ArticleInfo articleInfo = articleService.getArticleById(articleId);
    model.addAttribute("article", articleInfo);
    return "admin/article/articleEdit";
  }

  @PostMapping("/article/edit")
  public String updateArticle(@RequestParam Long articleId,
                              @ModelAttribute ArticleForm articleForm) {
    articleService.updateArticle(articleId, articleForm);
    return "redirect:/article/view?articleId=" + articleId;
  }

  /**
   * 게시글 삭제
   */
  @PostMapping("/article/delete")
  public String deleteArticle(@RequestParam Long articleId) {
    articleService.deleteArticle(articleId);
    return "redirect:/";
  }
}
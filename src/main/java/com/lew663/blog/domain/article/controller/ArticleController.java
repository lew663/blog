package com.lew663.blog.domain.article.controller;

import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.service.ArticleService;
import com.lew663.blog.domain.category.service.CategoryService;
import com.lew663.blog.domain.comment.dto.CommentForm;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import com.lew663.blog.global.config.LayoutConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

  private final CategoryService categoryService;
  private final ArticleService articleService;
  private final LayoutConfig layoutConfig;

  /**
   * 게시글 조회
   */

  // 전체글 조회
  @GetMapping("/article/list")
  public String readArticle(Model model) {
    layoutConfig.AddLayoutTo(model);
    List<ArticleInfo> articleList = articleService.getAllArticles();
    model.addAttribute("articles", articleList);
    model.addAttribute("categories", categoryService.findAllCategories());
    return "article/articleList";
  }

  // id로 조회
  @GetMapping("/article/view")
  public String readArticle(@RequestParam Long articleId,
                            @AuthenticationPrincipal PrincipalDetail principalDetail,
                            Model model) {
    layoutConfig.AddLayoutTo(model);
    ArticleInfo articleInfo = articleService.getArticleById(articleId);
    boolean isAdmin = false;
    if (principalDetail != null) {
      isAdmin = principalDetail.getAuthorities().stream()
          .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
    model.addAttribute("article", articleInfo);
    model.addAttribute("isAdmin", isAdmin);
    model.addAttribute("categories", categoryService.findAllCategories());
    model.addAttribute("commentForm", new CommentForm());
    return "article/articleView";
  }

  // 카테고리로 조회
  @GetMapping("/article/list/category")
  public String readArticleListByCategory(@RequestParam String categoryTitle,
                                          Model model) {
    layoutConfig.AddLayoutTo(model);
    List<ArticleInfo> articleList = articleService.getArticleByCategoryTitle(categoryTitle);
    model.addAttribute("categoryTitle", categoryTitle);
    model.addAttribute("articles", articleList);
    model.addAttribute("categories", categoryService.findAllCategories());
    return "article/articleListByCategory";
  }

  /**
   * 게시글 작성
   */
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
    model.addAttribute("categories", categoryService.findAllCategories());
    return "article/articleEdit";
  }
  @PostMapping("/article/edit")
  public String updateArticle(@RequestParam Long articleId,
                              @ModelAttribute ArticleForm articleForm) {
    articleService.updateArticle(articleId, articleForm);
    return "redirect:/article/view?articleId=" + articleId;
  }
}

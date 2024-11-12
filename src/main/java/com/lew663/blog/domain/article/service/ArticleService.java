package com.lew663.blog.domain.article.service;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.Tags;
import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.dto.ArticleSummaryInfo;
import com.lew663.blog.domain.article.repository.ArticleRepository;
import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.repository.CategoryRepository;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public ArticleInfo createArticle(ArticleForm articleForm, String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    Category category = null;
    if (articleForm.getCategoryId() != null && articleForm.getCategoryId() > 0) {
      category = categoryRepository.findById(articleForm.getCategoryId())
          .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    Article article = new Article(articleForm.getTitle(), articleForm.getContent(), member, category);
    if (articleForm.getTags() != null) {
      for (String tagName : articleForm.getTags()) {
        Tags tag = new Tags(tagName);
        article.addTag(tag);
      }
    }
    articleRepository.save(article);
    return ArticleInfo.from(article);
  }

  @Transactional
  public void updateArticle(Long articleId, ArticleForm articleForm) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    article.edit(articleForm.getContent(), articleForm.getTitle(),
        articleForm.getCategoryId() != null ? categoryRepository.findById(articleForm.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found")) : null);
    article.getArticleTagLists().clear();
    if (articleForm.getTags() != null) {
      for (String tagName : articleForm.getTags()) {
        Tags tag = new Tags(tagName);
        article.addTag(tag);
      }
    }
  }

  @Transactional
  public void deleteArticle(Long articleId) {
    articleRepository.deleteById(articleId);
  }

  @Transactional(readOnly = true)
  public Page<ArticleSummaryInfo> getAllArticles(Pageable pageable) {
    return articleRepository.findAllArticles(pageable);
  }

  @Transactional(readOnly = true)
  public Page<ArticleSummaryInfo> getArticleByCategoryTitle(String categoryTitle, Pageable pageable) {
    Category category = categoryRepository.findByTitle(categoryTitle)
        .orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));
    return articleRepository.findByCategory(category, pageable);
  }

  @Transactional(readOnly = true)
  public ArticleInfo getArticleById(Long id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    article.incrementViewCount();
    return ArticleInfo.from(article);
  }


}
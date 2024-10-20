package com.lew663.blog.domain.article.service;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.Tags;
import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.dto.ArticleInfo;
import com.lew663.blog.domain.article.repository.ArticleRepository;
import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.category.repository.CategoryRepository;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    Category category = categoryRepository.findById(articleForm.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

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
  public void updateArticle(Long id, ArticleForm articleForm, String email) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    if (!article.getMember().getEmail().equals(email)) {
      throw new RuntimeException("수정 권한이 없습니다.");
    }
    Category category = categoryRepository.findById(articleForm.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

    article.edit(articleForm.getContent(), articleForm.getTitle(), category);
  }

  @Transactional
  public void deleteArticle(Long id, String email) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    if (!article.getMember().getEmail().equals(email)) {
      throw new RuntimeException("삭제 권한이 없습니다.");
    }
    articleRepository.delete(article);
  }

  @Transactional
  public ArticleInfo getArticleById(Long id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    article.addHit();
    return ArticleInfo.from(article);
  }

  public List<ArticleInfo> getAllArticles() {
    return articleRepository.findAll().stream()
        .map(ArticleInfo::from)
        .collect(Collectors.toList());
  }

  @Transactional
  public void addTagToArticle(Long articleId, Tags tag) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    article.addTag(tag);
  }

}
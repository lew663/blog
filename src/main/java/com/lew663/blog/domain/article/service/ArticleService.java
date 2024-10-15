package com.lew663.blog.domain.article.service;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.dto.ArticleForm;
import com.lew663.blog.domain.article.repository.ArticleRepository;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final MemberRepository memberRepository;

  // 게시글 생성
  public Article createArticle(ArticleForm articleForm, String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Member not found"));

    Article article = new Article(articleForm.getTitle(), articleForm.getContent(), member);
    return articleRepository.save(article);
  }

  // 게시글 수정
  @Transactional
  public void updateArticle(Long id, ArticleForm articleForm, String email) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    if (article.getMember() == null) {
      throw new RuntimeException("작성자가 존재하지 않습니다.");
    }
    if (!article.getMember().getEmail().equals(email)) {
      throw new RuntimeException("수정 권한이 없습니다.");
    }
    article.edit(articleForm.getContent(), articleForm.getTitle());
  }

  // 게시글 삭제
  @Transactional
  public void deleteArticle(Long id, String email) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    // 작성자만 삭제 가능
    if (!article.getMember().getEmail().equals(email)) {
      throw new RuntimeException("삭제 권한이 없습니다.");
    }
    articleRepository.delete(article);
  }

  // 게시글 조회 (특정 게시물)
  public Article getArticleById(Long id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    article.addHit();
    return article;
  }

  // 게시글 조회 (전체 게시글)
  public List<Article> getAllArticles() {
    return articleRepository.findAll();
  }
}
package com.lew663.blog.domain.Article.service;

import com.lew663.blog.domain.Article.Article;
import com.lew663.blog.domain.Article.dto.ArticleDto;
import com.lew663.blog.domain.Article.repository.ArticleRepository;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final MemberRepository memberRepository;

  // 게시글 생성
  public Article createArticle(ArticleDto articleDto) {
    Member member = memberRepository.findById(articleDto.getMemberId())
        .orElseThrow(() -> new RuntimeException("Member not found"));

    Article article = new Article(articleDto.getTitle(), articleDto.getContent(), member);
    return articleRepository.save(article);
  }

  // 게시글 수정
  public Article updateArticle(Long id, ArticleDto articleDto) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));

    article.edit(articleDto.getContent(), articleDto.getTitle());
    return articleRepository.save(article);
  }

  // 게시글 삭제
  public void deleteArticle(Long id) {
    articleRepository.deleteById(id);
  }

  // 게시글 조회 (특정 게시물)
  public Article getArticleById(Long id) {
    return articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Article not found"));
  }

  // 게시글 조회 (전체 게시글)
  public List<Article> getAllArticles() {
    return articleRepository.findAll();
  }

  // 토큰에서 이메일 값 가져오는 코드
  private String getEmail() {
    return "";
  }
}
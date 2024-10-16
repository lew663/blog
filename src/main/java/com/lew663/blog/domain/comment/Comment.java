package com.lew663.blog.domain.comment;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id")
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private String content;

  public Comment(Article article, Member member, String content) {
    this.article = article;
    this.member = member;
    this.content = content;
  }

  public void updateContent(String content) {
    this.content = content;
  }
}
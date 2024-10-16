package com.lew663.blog.domain.article;

import com.lew663.blog.domain.member.Member;
import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BasicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id")
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false, columnDefinition = "bigint default 0")
  private long hit = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Builder
  public Article(String title, String content, Member member) {
    this.title = title;
    this.content = content;
    this.member = member;
  }

  public void edit(String content, String title) {
    this.content = content;
    this.title = title;
  }

  public void addHit(){
    this.hit++;
  }
}

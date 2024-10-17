package com.lew663.blog.domain.article;

import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ArticleViewCount extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hits_id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "article_id", nullable = false, unique = true)
  private Article article;

  @Column(nullable = false)
  private long viewCount = 0;

  public ArticleViewCount() {}

  public ArticleViewCount(Article article) {
    this.article = article;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }
}

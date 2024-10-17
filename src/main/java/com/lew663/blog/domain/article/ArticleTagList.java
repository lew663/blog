package com.lew663.blog.domain.article;

import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ArticleTagList extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "article_id")
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "tag_id")
  private Tags tags;

  public ArticleTagList() {}

  public ArticleTagList(Article article, Tags tags) {
    this.article = article;
    this.tags = tags;
    tags.getArticleTagLists().add(this);
  }
}

package com.lew663.blog.domain.comment;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BasicEntity {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Comment> child = new ArrayList<>();

  public Comment(Article article, Member member, String content) {
    this.article = article;
    this.member = member;
    this.content = content;
  }

  public Comment(Article article, Member member, String content, Comment parent) {
    this(article, member, content);
    this.parent = parent;
  }

  public void updateContent(String content) {
    this.content = content;
  }
}
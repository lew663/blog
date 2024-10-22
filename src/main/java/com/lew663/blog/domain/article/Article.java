package com.lew663.blog.domain.article;

import com.lew663.blog.domain.category.Category;
import com.lew663.blog.domain.comment.Comment;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

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

  @OneToOne(mappedBy = "article", cascade = CascadeType.ALL)
  private ArticleViewCount viewCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private List<ArticleTagList> articleTagLists = new ArrayList<>();

  @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Comment> parentCommentList = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = true)
  private Category category;

  @Builder
  public Article(String title, String content, Member member, Category category) {
    this.title = title;
    this.content = content;
    this.member = member;
    this.category = category;
  }

  public void edit(String content, String title, Category category) {
    this.content = content;
    this.title = title;
    this.category = category;
  }

  public void addHit(){
    if (viewCount == null) {
      viewCount = new ArticleViewCount(this);
    }
    viewCount.incrementViewCount();
  }

  public void addTag(Tags tag) {
    ArticleTagList articleTagList = new ArticleTagList(this, tag);
    articleTagLists.add(articleTagList);
    tag.getArticleTagLists().add(articleTagList);
  }
}

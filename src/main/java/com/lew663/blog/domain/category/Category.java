package com.lew663.blog.domain.category;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Category extends BasicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  @Column(nullable = false, unique = true, length = 20)
  private String title;

  @OneToMany(mappedBy = "category")
  private List<Article> articles = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private List<Category> child = new ArrayList<>();

  // 기본 생성자
  public Category() {}

  public Category(String title) {
    this.title = title;
  }

  public void setParent(Category parent) {
    this.parent = parent;
    if (!parent.getChild().contains(this)) {
      parent.addChild(this);
    }
  }

  public void addChild(Category child) {
    this.child.add(child);
    child.setParent(this);
  }

  public void changeTitle(String title) {
    this.title = title;
  }
}

package com.lew663.blog.domain.article;

import com.lew663.blog.global.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Tags extends BasicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tags_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "tags", cascade = CascadeType.ALL)
  private List<ArticleTagList> articleTagLists = new ArrayList<>();

  public Tags() {}

  public Tags(String name) {
    this.name = name;
  }
}

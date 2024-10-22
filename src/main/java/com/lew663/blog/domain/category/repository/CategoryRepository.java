package com.lew663.blog.domain.category.repository;

import com.lew663.blog.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findAllByParentIsNull();
}

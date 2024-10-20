package com.lew663.blog.domain.category.repository;

import com.lew663.blog.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

package com.lew663.blog.domain.comment.repository;

import com.lew663.blog.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByArticleId(Long articleId);
}

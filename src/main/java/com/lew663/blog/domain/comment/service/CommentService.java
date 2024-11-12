package com.lew663.blog.domain.comment.service;

import com.lew663.blog.domain.article.Article;
import com.lew663.blog.domain.article.repository.ArticleRepository;
import com.lew663.blog.domain.comment.Comment;
import com.lew663.blog.domain.comment.dto.CommentForm;
import com.lew663.blog.domain.comment.dto.CommentInfo;
import com.lew663.blog.domain.comment.repository.CommentRepository;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;
  private final MemberRepository memberRepository;

  @Transactional
  public CommentInfo createComment(Long articleId, String email, CommentForm commentForm, Long parentId) {

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new IllegalArgumentException("Article not found"));
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Member not found"));

    Comment comment = (parentId != null)
        ? createChildComment(article, member, commentForm, parentId)
        : new Comment(article, member, commentForm.getContent());

    commentRepository.save(comment);
    return CommentInfo.from(comment);
  }

  @Transactional(readOnly = true)
  public List<CommentInfo> getCommentsByArticleId(Long articleId) {
    return commentRepository.findByArticleId(articleId)
        .stream()
        .map(CommentInfo::from)
        .collect(Collectors.toList());
  }

  private Comment createChildComment(Article article, Member member, CommentForm commentForm, Long parentId) {
    Comment parentComment = commentRepository.findById(parentId)
        .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
    Comment childComment = new Comment(article, member, commentForm.getContent(), parentComment);
    parentComment.getChild().add(childComment);
    return childComment;
  }
}
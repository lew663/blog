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
  public CommentInfo createComment(Long articleId, String email, CommentForm commentForm) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new RuntimeException("Article not found"));
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    Comment comment = new Comment(article, member, commentForm.getContent());
    commentRepository.save(comment);
    return CommentInfo.from(comment);
  }

  @Transactional
  public void updateComment(Long commentId, String email, CommentForm commentForm) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("Comment not found"));
    if (!comment.getMember().getEmail().equals(email)) {
      throw new RuntimeException("수정 권한이 없습니다.");
    }
    comment.updateContent(commentForm.getContent());
  }

  @Transactional
  public void deleteComment(Long commentId, String email) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("Comment not found"));
    if (!comment.getMember().getEmail().equals(email)) {
      throw new RuntimeException("삭제 권한이 없습니다.");
    }
    commentRepository.delete(comment);
  }

  public List<CommentInfo> getCommentsByArticleId(Long articleId) {
    List<Comment> comments = commentRepository.findByArticleId(articleId);
    return comments.stream()
        .map(CommentInfo::from)
        .collect(Collectors.toList());
  }
}
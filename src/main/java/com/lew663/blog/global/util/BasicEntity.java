package com.lew663.blog.global.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BasicEntity {

  @CreatedDate
  @Column(updatable = false)
  private LocalDate createdDate;

  @LastModifiedDate
  private LocalDate  updatedDate;
}
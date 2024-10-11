package com.lew663.blog.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  private String imageUrl;
  private String providerId;

  @Enumerated(EnumType.STRING)
  private Provider provider;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String refreshToken;

  public void passwordEncode(PasswordEncoder passwordEncoder){
    this.password = passwordEncoder.encode(this.password);
  }

  public void updateRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}

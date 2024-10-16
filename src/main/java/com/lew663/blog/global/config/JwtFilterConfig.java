package com.lew663.blog.global.config;

import com.lew663.blog.global.jwt.JwtFilter;
import com.lew663.blog.global.jwt.JwtTokenProvider;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtFilterConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Bean
  public JwtFilter jwtFilter() {
    return new JwtFilter(jwtTokenProvider, memberRepository);
  }
}
package com.lew663.blog.config;

import com.lew663.blog.jwt.JwtFilter;
import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.member.repository.MemberRepository;
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
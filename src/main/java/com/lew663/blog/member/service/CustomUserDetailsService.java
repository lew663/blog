package com.lew663.blog.member.service;

import com.lew663.blog.member.domain.Member;
import com.lew663.blog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

    return org.springframework.security.core.userdetails.User.builder()
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getRole().name())
        .build();
  }
}
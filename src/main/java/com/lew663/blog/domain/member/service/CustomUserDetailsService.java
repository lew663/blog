package com.lew663.blog.domain.member.service;

import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.dto.CustomUserDetails;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

    return new CustomUserDetails(
        member.getEmail(), // 이메일
        member.getPassword(), // 비밀번호
        List.of(new SimpleGrantedAuthority(member.getRole().name())) // 권한
    );
  }
}

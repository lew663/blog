package com.lew663.blog.domain.member.service;

import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.dto.SignUpDto;
import com.lew663.blog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public void signup(SignUpDto signUpDto) throws Exception {
    check(signUpDto);
    Member member = signUpDto.toEntity();
    member.passwordEncode(passwordEncoder);
    saveMember(member);
  }

  private void saveMember(Member member) {
    memberRepository.save(member);
  }

  private void check(SignUpDto signUpDto) throws Exception {
    if(memberRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
      throw new Exception("이미 존재하는 이메일 입니다.");
    }
    if(!signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
      throw new Exception("비밀번호와 확인 비밀번호가 일치하지 않습니다.");
    }
  }
}

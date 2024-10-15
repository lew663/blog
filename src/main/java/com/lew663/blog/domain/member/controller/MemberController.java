package com.lew663.blog.domain.member.controller;

import com.lew663.blog.domain.member.dto.SignUpForm;
import com.lew663.blog.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/signup")
  public String signup(@Valid @RequestBody SignUpForm signUpForm) throws Exception {
    memberService.signup(signUpForm);
    return "회원 가입 성공!";
  }
}

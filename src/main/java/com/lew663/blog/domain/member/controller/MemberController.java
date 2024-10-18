package com.lew663.blog.domain.member.controller;

import com.lew663.blog.domain.member.dto.SignUpForm;
import com.lew663.blog.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/signup")
  public String singUpForm() {
    return "member/signup";
  }

  @PostMapping("/signup")
  public String signup(@Valid @ModelAttribute SignUpForm signUpForm) throws Exception {
    memberService.signup(signUpForm);
    return "redirect:/login";
  }
}

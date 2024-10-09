package com.lew663.blog.member.controller;

import com.lew663.blog.member.dto.SignUpDto;
import com.lew663.blog.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;

  // 회원가입 폼 페이지로 이동
  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("signUpDto", new SignUpDto());
    return "member/signup";
  }

  // 회원가입 요청 처리
  @PostMapping("/signup")
  public String signup(@Valid SignUpDto signUpDto, BindingResult bindingResult, Model model) {
    // 폼 입력 유효성 검사 오류가 있는 경우
    if (bindingResult.hasErrors()) {
      return "member/signup";
    }

    try {
      memberService.signup(signUpDto); // 회원가입 로직 수행
    } catch (Exception e) {
      // 예외 발생 시 에러 메시지 전달
      model.addAttribute("errorMessage", e.getMessage());
      return "member/signup";
    }

    // 회원가입 완료 후 리다이렉트
    return "redirect:/member/login";
  }

  // 로그인 폼 페이지로 이동
  @GetMapping("/login")
  public String loginForm() {
    return "member/login";
  }
}

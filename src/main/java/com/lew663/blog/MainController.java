package com.lew663.blog;

import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @GetMapping("/")
  public String mainPage(HttpServletRequest request, Model model) {
    // Member 객체를 생성하고 모델에 추가
    String token = jwtTokenProvider.extractAccessToken(request).orElse(null);
    if (token != null && jwtTokenProvider.isTokenValid(token)) {
      // 토큰에서 이메일 추출
      Optional<String> emailOpt = jwtTokenProvider.extractEmail(token);
      emailOpt.ifPresentOrElse(email -> {
        memberRepository.findByEmail(email).ifPresent(member -> {
          model.addAttribute("member", member);
          log.info("사용자 정보를 찾았습니다: {}", member);
        });
      }, () -> {
        log.warn("이메일을 추출하지 못했습니다.");
      });
    }
    return "index";
  }
}

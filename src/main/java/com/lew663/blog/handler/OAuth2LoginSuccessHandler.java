package com.lew663.blog.handler;

import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.member.domain.Member;
import com.lew663.blog.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getAttribute("email");

    String accessToken = jwtTokenProvider.createAccessToken(email);
    String refreshToken = jwtTokenProvider.createRefreshToken();

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    // 사용자 정보를 가져와서 refreshToken 업데이트
    Optional<Member> memberOptional = memberRepository.findByEmail(email);
    if (memberOptional.isPresent()) {
      Member member = memberOptional.get();
      member.updateRefreshToken(refreshToken);
      memberRepository.saveAndFlush(member);
      log.info("OAuth2 로그인 성공: {} - RefreshToken 업데이트 완료", email);
    } else {
      log.warn("OAuth2 로그인 시 회원 정보를 찾을 수 없습니다: {}", email);
    }

    log.info("OAuth2 로그인에 성공하였습니다. 이메일 : {}", email);
    log.info("발급된 AccessToken : {}", accessToken);
    response.sendRedirect("/");
  }
}
package com.lew663.blog.global.handler;

import com.lew663.blog.global.jwt.JwtTokenProvider;
import com.lew663.blog.domain.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Value("${jwt.access.expiration}")
  private String accessTokenExpiration;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    String email = authentication.getName();

    String role = authentication.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElseThrow(() -> new IllegalStateException("권한이 없습니다."));

    String accessToken = jwtTokenProvider.createAccessToken(email, role);
    String refreshToken = jwtTokenProvider.createRefreshToken();

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    updateRefreshToken(email, refreshToken);

    log.info("로그인 성공 - 이메일: {}, AccessToken: {}", email, accessToken);
    response.sendRedirect("/");
  }

  private void updateRefreshToken(String email, String refreshToken) {
    memberRepository.findByEmail(email).ifPresent(member -> {
      member.updateRefreshToken(refreshToken);
      memberRepository.save(member);
    });
  }
}

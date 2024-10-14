package com.lew663.blog.handler;

import com.lew663.blog.domain.member.dto.PrincipalDetail;
import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.domain.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    String email = extractEmail(authentication);
    String accessToken = jwtTokenProvider.createAccessToken(email);
    String refreshToken = jwtTokenProvider.createRefreshToken();

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    updateRefreshToken(email, refreshToken);

    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
    log.info("발급된 AccessToken : {}", accessToken);
    log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

    response.sendRedirect("/");
  }

  private String extractEmail(Authentication authentication) {
    log.info("로그인 타입 : {}", authentication.getPrincipal().getClass());
    if (authentication.getPrincipal() instanceof OAuth2User) {
      return (String) ((OAuth2User) authentication.getPrincipal()).getName();
    } else if (authentication.getPrincipal() instanceof PrincipalDetail) {
      return ((PrincipalDetail) authentication.getPrincipal()).getUsername();
    } else {
      log.warn("Unknown authentication principal type: {}", authentication.getPrincipal().getClass());
      throw new IllegalArgumentException("Unknown authentication principal type");
    }
  }

  private void updateRefreshToken(String email, String refreshToken) {
    memberRepository.findByEmail(email).ifPresent(member -> {
      member.updateRefreshToken(refreshToken);
      memberRepository.saveAndFlush(member);
    });
  }
}

package com.lew663.blog.handler;

import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
                                      Authentication authentication) {
    String email = extractUsername(authentication);
    String accessToken = jwtTokenProvider.createAccessToken(email);
    String refreshToken = jwtTokenProvider.createRefreshToken();

    jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    memberRepository.findByEmail(email)
        .ifPresent(member -> {
          member.updateRefreshToken(refreshToken);
          memberRepository.saveAndFlush(member);
        });
    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
    log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
    log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
  }

  private String extractUsername(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }
}

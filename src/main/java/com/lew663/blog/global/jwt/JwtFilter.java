package com.lew663.blog.global.jwt;

import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.dto.PrincipalDetail;
import com.lew663.blog.domain.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    log.info("JWT 필터 실행: {}", request.getRequestURI());

    // 쿠키에서 AccessToken 추출
    String accessToken = extractTokenFromCookies(request);
    if (accessToken != null && jwtTokenProvider.isTokenValid(accessToken)) {
      jwtTokenProvider.extractEmail(accessToken).flatMap(memberRepository::findByEmail).ifPresent(member -> {
        jwtTokenProvider.extractRole(accessToken).ifPresent(role -> {
          saveAuthentication(member, role);
        });
      });
    }

    filterChain.doFilter(request, response);
  }

  /**
   * 쿠키에서 토큰 추출
   */
  private String extractTokenFromCookies(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if (cookie.getName().equals("accessToken")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  /**
   * 사용자 인증 정보를 SecurityContext 에 저장
   */
  private void saveAuthentication(Member member, String role) {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

    PrincipalDetail principalDetail = PrincipalDetail.builder()
        .email(member.getEmail())
        .password(member.getPassword())
        .authorities(List.of(authority))
        .attributes(null)
        .build();

    Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.info("사용자 인증 정보 SecurityContext 에 저장: {}", member.getEmail());
  }
}

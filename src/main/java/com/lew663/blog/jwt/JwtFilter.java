package com.lew663.blog.jwt;

import com.lew663.blog.member.domain.Member;
import com.lew663.blog.member.repository.MemberRepository;
import com.lew663.blog.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private static final String NO_CHECK = "/login";

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    // 로그인 요청은 필터 통과 시킴
    if (request.getRequestURI().equals(NO_CHECK)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 사용자 요청 헤더에서 RefreshToken 추출
    String refreshToken = jwtTokenProvider.extractRefreshToken(request)
        .filter(jwtTokenProvider::isTokenValid)
        .orElse(null);

    // RefreshToken 값이 존재하는 경우
    if (refreshToken != null) {
      checkRefreshTokenReissueAccessToken(response, refreshToken);
      return;
    }
    // RefreshToken 값이 null 인 경우
    checkAccessTokenAndAuthentication(request, response, filterChain);
  }

  /**
   * RefreshToken 을 사용하여 사용자 찾기 및 AccessToken 재발급
   *
   * @param response     HTTP 응답 객체
   * @param refreshToken 유효성을 검사할 RefreshToken
   */
  public void checkRefreshTokenReissueAccessToken(HttpServletResponse response, String refreshToken) {
    Optional<Member> optionalUser = memberRepository.findByRefreshToken(refreshToken);

    if (optionalUser.isPresent()) {
      Member member = optionalUser.get();
      String reissuedRefreshToken = reIssueRefreshToken(member);

      // AccessToken 과 재발급된 RefreshToken 을 클라이언트에 전송
      jwtTokenProvider.sendAccessAndRefreshToken(response,
          jwtTokenProvider.createAccessToken(member.getEmail()),
          reissuedRefreshToken
      );
    } else {
      log.error("유효하지 않은 리프레시 토큰입니다.");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * AccessToken 검증 및 인증 처리
   *
   * @param request  HTTP 요청 객체
   * @param response HTTP 응답 객체
   * @param filterChain 필터 체인
   */
  public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                FilterChain filterChain) throws ServletException, IOException {
    log.info("AccessToken 검증 시작");
    jwtTokenProvider.extractAccessToken(request)
        .filter(jwtTokenProvider::isTokenValid).flatMap(jwtTokenProvider::extractEmail).ifPresent(email -> {
          memberRepository.findByEmail(email)
              .ifPresentOrElse(this::saveAuthentication,
                  () -> log.error("사용자를 찾을 수 없습니다: {}", email));
        });

    filterChain.doFilter(request, response);
  }

  /**
   * 사용자 인증 정보를 SecurityContext에 저장
   *
   * @param member 인증할 사용자 객체
   */
  public void saveAuthentication(Member member) {
    String password = member.getPassword();
    // 비밀번호가 없으면 랜덤 비밀번호 생성 (소셜 로그인은 비밀번호가 null 이기 때문)
    if (password == null) {
      log.warn("소셜 로그인 사용자로 랜덤 비밀번호 생성: {}", member.getEmail());
      password = PasswordUtil.generateRandomPassword();
    }

    UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
        .username(member.getEmail())
        .password(password)
        .roles(member.getRole().name())
        .build();

    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * RefreshToken 재발급 및 DB에 업데이트
   *
   * @param member 사용자 객체
   * @return 재발급된 RefreshToken
   */
    private String reIssueRefreshToken (Member member){
      String reIssuedRefreshToken = jwtTokenProvider.createRefreshToken();
      member.updateRefreshToken(reIssuedRefreshToken);
      memberRepository.saveAndFlush(member);
      return reIssuedRefreshToken;
    }
  }

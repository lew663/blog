package com.lew663.blog.jwt;

import com.lew663.blog.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Getter
@Transactional
@Slf4j
public class JwtTokenProvider {

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  private SecretKey key;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String BEARER = "Bearer ";

  private final MemberRepository memberRepository;

  @PostConstruct
  public void init() {
    if (secretKey == null || secretKey.isEmpty()) {
      throw new IllegalArgumentException("JWT secretKey 는 null 또는 비어있을 수 없습니다.");
    }
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * AccessToken 생성 메소드
   */
  public String createAccessToken(String email) {
    Date now = new Date();
    return Jwts.builder()
        .subject(ACCESS_TOKEN_SUBJECT)
        .expiration(new Date(now.getTime() + accessTokenExpirationPeriod))
        .claim(EMAIL_CLAIM, email)
        .signWith(key)
        .compact();
  }

  /**
   * RefreshToken 생성
   */
  public String createRefreshToken() {
    Date now = new Date();
    return Jwts.builder()
        .subject(REFRESH_TOKEN_SUBJECT)
        .expiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
        .signWith(key)
        .compact();
  }

  /**
   * AccessToken 헤더에 실어서 보내기
   */
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(accessHeader, accessToken);
    log.info("재발급된 Access Token : {}", accessToken);
  }

  /**
   * AccessToken + RefreshToken 헤더에 실어서 보내기
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("Access Token, Refresh Token 헤더 설정 완료");
  }

  /**
   * 헤더에서 RefreshToken 추출
   */
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader))
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  /**
   * 헤더에서 AccessToken 추출
   */
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(accessToken -> accessToken.startsWith(BEARER))
        .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  /**
   * AccessToken 에서 Email 추출
   */
  public Optional<String> extractEmail(String accessToken) {
    try {
      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(accessToken);
      return Optional.ofNullable(claimsJws.getPayload().get(EMAIL_CLAIM, String.class));
    } catch (Exception e) {
      log.error("액세스 토큰이 유효하지 않습니다.");
      return Optional.empty();
    }
  }

  /**
   * AccessToken 헤더 설정
   */
  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }

  /**
   * RefreshToken 헤더 설정
   */
  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  /**
   * RefreshToken DB 저장(업데이트)
   */
  public void updateRefreshToken(String email, String refreshToken) {
    memberRepository.findByEmail(email)
        .ifPresentOrElse(
            member -> member.updateRefreshToken(refreshToken),
            () -> new RuntimeException("일치하는 회원이 없습니다.")
        );
  }

  /**
   * JWT 유효성 검사
   */
  public boolean isTokenValid(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }
}

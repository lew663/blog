package com.lew663.blog.global.jwt;

import com.lew663.blog.domain.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
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
import java.util.List;
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

  private SecretKey key;

  private static final String EMAIL_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";
  private static final String ACCESS_TOKEN_COOKIE = "accessToken";
  private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

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
  public String createAccessToken(String email, String role) {
    return Jwts.builder()
        .subject("AccessToken")
        .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
        .claim(EMAIL_CLAIM, email)
        .claim(ROLES_CLAIM, role)
        .signWith(key)
        .compact();
  }

  /**
   * RefreshToken 생성
   */
  public String createRefreshToken() {
    return Jwts.builder()
        .subject("RefreshToken")
        .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
        .signWith(key)
        .compact();
  }

  /**
   * AccessToken + RefreshToken 을 쿠키에 저장하여 클라이언트에게 전송
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    addCookie(response, ACCESS_TOKEN_COOKIE, accessToken, accessTokenExpirationPeriod);
    addCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, refreshTokenExpirationPeriod);
    log.info("AccessToken 및 RefreshToken 쿠키에 저장 완료");
  }

  public void deleteTokens(HttpServletResponse response) {
    addCookie(response, ACCESS_TOKEN_COOKIE, "", 0L); // 만료된 쿠키로 설정
    addCookie(response, REFRESH_TOKEN_COOKIE, "", 0L); // 만료된 쿠키로 설정
    log.info("AccessToken 및 RefreshToken 쿠키 삭제 완료");
  }

  /**
   * 쿠키 생성 메소드
   */
  private void addCookie(HttpServletResponse response, String name, String value, Long maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(Math.toIntExact(maxAge / 1000));
    response.addCookie(cookie);
  }

  /**
   * AccessToken 에서 Email 추출
   */
  public Optional<String> extractEmail(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return Optional.ofNullable(claimsJws.getPayload().get(EMAIL_CLAIM, String.class));
    } catch (Exception e) {
      log.error("액세스 토큰이 유효하지 않습니다.");
      return Optional.empty();
    }
  }

  /**
   * AccessToken 에서 권한 정보 추출
   */
  public Optional<String> extractRole(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return Optional.ofNullable(claimsJws.getPayload().get(ROLES_CLAIM, String.class));
    } catch (Exception e) {
      log.error("액세스 토큰에서 권한 정보를 추출할 수 없습니다.");
      return Optional.empty();
    }
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

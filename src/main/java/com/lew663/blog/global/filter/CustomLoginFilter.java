package com.lew663.blog.global.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew663.blog.global.handler.LoginFailureHandler;
import com.lew663.blog.global.handler.LoginSuccessHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final String DEFAULT_LOGIN_REQUEST_URL = "/member/login";
  private static final String HTTP_METHOD = "POST";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

  private final LoginSuccessHandler successHandler;
  private final LoginFailureHandler failureHandler;

  public CustomLoginFilter(AuthenticationManager authenticationManager,
                           LoginSuccessHandler successHandler,
                           LoginFailureHandler failureHandler) {
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
    setAuthenticationManager(authenticationManager);
    setRequiresAuthenticationRequestMatcher(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    String email;
    String password;

    // 요청이 JSON 형식인지 확인
    if ("application/json".equals(request.getContentType())) {
      try {
        // JSON 요청의 내용을 읽어서 파라미터를 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {});
        email = requestBody.get("email");
        password = requestBody.get("password");
      } catch (IOException e) {
        log.error("요청 본문을 읽는 데 오류 발생: {}", e.getMessage());
        throw new AuthenticationServiceException("Invalid request body");
      }
    } else {
      // URL 인코딩된 요청의 경우
      email = request.getParameter("email");
      password = request.getParameter("password");
    }

    log.info("사용자 인증 시도: {}", email);

    // UsernamePasswordAuthenticationToken 생성
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
    return getAuthenticationManager().authenticate(authRequest);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult) throws IOException, ServletException {
    log.info("사용자 인증 성공: {}", authResult.getName());
    successHandler.onAuthenticationSuccess(request, response, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    log.warn("인증 실패: {}", failed.getMessage());
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}
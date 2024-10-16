package com.lew663.blog.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/plain;charset=UTF-8");

    // 소셜 로그인인지 일반 로그인인지 확인
    String errorMessage;
    if (request.getRequestURI().contains("/oauth2")) {
      errorMessage = "소셜 로그인 실패! 서버 로그를 확인해주세요.";
      log.info("소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
    } else {
      errorMessage = "로그인 실패! 이메일이나 비밀번호를 확인해주세요.";
      log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }

    response.getWriter().write(errorMessage);
  }
}

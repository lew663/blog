package com.lew663.blog.global.config;

import com.lew663.blog.global.filter.CustomLoginFilter;
import com.lew663.blog.global.handler.LoginFailureHandler;
import com.lew663.blog.global.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@RequiredArgsConstructor
public class CustomLoginFilterConfig {

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;

  @Bean
  public CustomLoginFilter customLoginFilter(@Lazy AuthenticationManager authenticationManager) {
    return new CustomLoginFilter(authenticationManager, loginSuccessHandler, loginFailureHandler);
  }
}
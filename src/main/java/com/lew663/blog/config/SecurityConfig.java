package com.lew663.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew663.blog.filter.CustomLoginFilter;
import com.lew663.blog.handler.LoginFailureHandler;
import com.lew663.blog.handler.LoginSuccessHandler;
import com.lew663.blog.jwt.JwtFilter;
import com.lew663.blog.jwt.JwtTokenProvider;
import com.lew663.blog.member.repository.MemberRepository;
import com.lew663.blog.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginService loginService;
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;
  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

        .authorizeHttpRequests(request -> request
            .requestMatchers("/", "/index", "/login").permitAll()
            .requestMatchers("/member/signup", "/member/login").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()
        );
    http.addFilterBefore(customLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(loginService);
    return new ProviderManager(provider);
  }

  @Bean
  public CustomLoginFilter customLoginFilter() {
    return new CustomLoginFilter(authenticationManager(), loginSuccessHandler(), loginFailureHandler());
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtTokenProvider, memberRepository);
  }

  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  @Bean
  public JwtFilter jwtFilter() {
    return new JwtFilter(jwtTokenProvider, memberRepository);
  }
}

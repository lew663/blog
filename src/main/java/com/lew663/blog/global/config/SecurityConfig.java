package com.lew663.blog.global.config;

import com.lew663.blog.domain.member.service.PrincipalUserDetailsService;
import com.lew663.blog.global.filter.CustomLoginFilter;
import com.lew663.blog.global.handler.LoginFailureHandler;
import com.lew663.blog.global.handler.LoginSuccessHandler;
import com.lew663.blog.global.jwt.JwtFilter;
import com.lew663.blog.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

  private final JwtFilter jwtFilter;
  private final CustomLoginFilter customLoginFilter;
  private final PrincipalUserDetailsService principalUserDetailsService;
  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

        .authorizeHttpRequests(request -> request
            .requestMatchers("/category/**", "/article/write", "/article/edit", "/article/delete").hasRole("ADMIN")
            .requestMatchers("/article/view/**", "/article/list/**").permitAll()
            .requestMatchers("/", "/static/**", "/member/login", "/login", "/member/signup", "/oauth2/authorization/**").permitAll()
            .requestMatchers("/assets/**", "/css/**", "/js/**", "/static/**", "/favicon.ico").permitAll()
            .anyRequest().authenticated()
        );
    http
        .oauth2Login(oauth2 -> oauth2
            .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/{registrationId}"))
            .userInfoEndpoint(endpoint -> endpoint.userService(principalUserDetailsService))
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
        )
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint((request, response, authException) -> {
              response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            })
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .addLogoutHandler((request, response, authentication) -> jwtTokenProvider.deleteTokens(response))
        );

    http.addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}

package com.lew663.blog.domain.member.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Builder
public class PrincipalDetail implements UserDetails, OAuth2User {

  private final String email; // 이메일
  private final String password; // 비밀번호
  private final Collection<? extends GrantedAuthority> authorities; // 권한
  private final Map<String, Object> attributes;

  public PrincipalDetail(String email, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.attributes = attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email; // 이메일을 username으로 사용
  }

  @Override
  public boolean isAccountNonExpired() {
    return true; // 만료되지 않음
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // 잠기지 않음
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // 자격증명 만료되지 않음
  }

  @Override
  public boolean isEnabled() {
    return true; // 활성화되어 있음
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes; // 소셜 로그인 정보 반환
  }

  @Override
  public String getName() {
    return email; // 사용자 이름으로 이메일 사용
  }
}

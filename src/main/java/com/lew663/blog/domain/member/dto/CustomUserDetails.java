package com.lew663.blog.domain.member.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
public class CustomUserDetails implements UserDetails {
  private final String email; // 이메일
  private final String password; // 비밀번호
  private final Collection<? extends GrantedAuthority> authorities; // 권한

  public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities) {
    this.email = email;
    this.password = password;
    this.authorities = authorities;
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
}

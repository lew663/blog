package com.lew663.blog.member.dto;

import com.lew663.blog.member.domain.Member;
import com.lew663.blog.member.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {

  @NotBlank(message = "이메일을 입력하세요.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;
  private String passwordCheck;

  public Member toEntity() {
    return Member.builder()
        .email(this.email)
        .password(this.password)
        .role(Role.USER)
        .build();
  }

}

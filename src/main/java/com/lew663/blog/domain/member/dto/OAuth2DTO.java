package com.lew663.blog.domain.member.dto;

import com.lew663.blog.domain.member.Role;
import com.lew663.blog.domain.member.Member;
import com.lew663.blog.domain.member.Provider;
import com.lew663.blog.domain.member.oauth2.GoogleOAuth2Info;
import com.lew663.blog.domain.member.oauth2.KakaoOAuth2Info;
import com.lew663.blog.domain.member.oauth2.NaverOAuth2Info;
import com.lew663.blog.domain.member.oauth2.OAuth2Info;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2DTO {

  private final String providerName; // 로그인 진행하는 소셜사이트 이름
  private final OAuth2Info oAuth2Info;

  @Builder
  public OAuth2DTO(String providerName, OAuth2Info oAuth2Info) {
    this.providerName = providerName;
    this.oAuth2Info = oAuth2Info;
  }

  public static OAuth2DTO of(Provider provider,
                             String providerName,
                             Map<String, Object> attributes
  ) {

    return switch (provider) {
      case NAVER -> fromNaver(providerName, attributes);
      case KAKAO -> fromKakao(providerName, attributes);
      default -> fromGoogle(providerName, attributes);
    };

  }

  public static OAuth2DTO fromNaver(String providerName, Map<String, Object> attributes) {
    return OAuth2DTO.builder()
        .providerName(providerName)
        .oAuth2Info(new NaverOAuth2Info(attributes))
        .build();
  }

  private static OAuth2DTO fromKakao(String providerName, Map<String, Object> attributes) {
    return OAuth2DTO.builder()
        .providerName(providerName)
        .oAuth2Info(new KakaoOAuth2Info(attributes))
        .build();
  }

  public static OAuth2DTO fromGoogle(String providerName, Map<String, Object> attributes) {
    return OAuth2DTO.builder()
        .providerName(providerName)
        .oAuth2Info(new GoogleOAuth2Info(attributes))
        .build();
  }

  public Member toMemberEntity(Provider provider, OAuth2Info oAuth2Info) {
    return Member.builder()
        .provider(provider)
        .providerId(oAuth2Info.getId())
        .email(oAuth2Info.getEmail())
        .imageUrl(oAuth2Info.getImageUrl())
        .role(Role.USER)
        .build();
  }
}

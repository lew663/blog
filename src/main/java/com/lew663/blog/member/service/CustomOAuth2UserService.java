package com.lew663.blog.member.service;

import com.lew663.blog.member.domain.Member;
import com.lew663.blog.member.domain.Provider;
import com.lew663.blog.member.dto.OAuth2DTO;
import com.lew663.blog.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String providerName = userRequest.getClientRegistration().getRegistrationId();
    Provider provider  = mapProviderName(providerName);

    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

    Map<String, Object> attributes = oAuth2User.getAttributes();

    OAuth2DTO extractAttributes = OAuth2DTO.of(provider, userNameAttributeName, attributes);

    Member createdUser = getUser(extractAttributes, provider);

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
        attributes,
        extractAttributes.getProviderName()
    );
  }
  private Provider mapProviderName(String providerName) {
    return switch (providerName.toLowerCase()) {
      case "naver" -> Provider.NAVER;
      case "kakao" -> Provider.KAKAO;
      default -> Provider.GOOGLE;
    };
  }

  private Member getUser(OAuth2DTO oAuth2DTO, Provider provider) {
    return memberRepository.findByProviderAndProviderId(provider,
            oAuth2DTO.getOAuth2Info().getId())
        .orElseGet(() -> saveUser(oAuth2DTO, provider));
  }

  private Member saveUser(OAuth2DTO oAuth2DTO, Provider provider) {
    Member createdUser = oAuth2DTO.toMemberEntity(provider, oAuth2DTO.getOAuth2Info());
    return memberRepository.save(createdUser);
  }
}

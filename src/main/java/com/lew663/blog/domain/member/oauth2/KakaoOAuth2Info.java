package com.lew663.blog.domain.member.oauth2;

import java.util.Map;

public class KakaoOAuth2Info extends OAuth2Info{

  /**
   *  Kakao response json 예시
   *
   *  {
   *     "id":123456789,
   *     "connected_at": "2022-04-11T01:45:28Z",
   *     "kakao_account": {
   *         // 프로필 또는 닉네임 동의항목 필요
   *         "profile_nickname_needs_agreement	": false,
   *         // 프로필 또는 프로필 사진 동의항목 필요
   *         "profile_image_needs_agreement	": false,
   *         "profile": {
   *             // 프로필 또는 닉네임 동의항목 필요
   *             "nickname": "홍길동",
   *             // 프로필 또는 프로필 사진 동의항목 필요
   *             "thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",
   *             "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg",
   *             "is_default_image":false,
   *             "is_default_nickname": false
   *         },
   *         // 이름 동의항목 필요
   *         "name_needs_agreement":false,
   *         "name":"홍길동",
   *         // 카카오계정(이메일) 동의항목 필요
   *         "email_needs_agreement":false,
   *         "is_email_valid": true,
   *         "is_email_verified": true,
   *         "email": "sample@sample.com",
   *         // 연령대 동의항목 필요
   *         "age_range_needs_agreement":false,
   *         "age_range":"20~29",
   *         // 출생 연도 동의항목 필요
   *         "birthyear_needs_agreement": false,
   *         "birthyear": "2002",
   *         // 생일 동의항목 필요
   *         "birthday_needs_agreement":false,
   *         "birthday":"1130",
   *         "birthday_type":"SOLAR",
   *         // 성별 동의항목 필요
   *         "gender_needs_agreement":false,
   *         "gender":"female",
   *         // 카카오계정(전화번호) 동의항목 필요
   *         "phone_number_needs_agreement": false,
   *         "phone_number": "+82 010-1234-5678",
   *         // CI(연계정보) 동의항목 필요
   *         "ci_needs_agreement": false,
   *         "ci": "${CI}",
   *         "ci_authenticated_at": "2019-03-11T11:25:22Z",
   *     },
   *     "properties":{
   *         "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
   *         ...
   *     },
   *     "for_partner": {
   *         "uuid": "${UUID}"
   *     }
   * }
   */
  public KakaoOAuth2Info(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return String.valueOf(attributes.get("id"));
  }

  @Override
  public String getEmail() {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

    if (account == null) {
      return null;
    }

    return (String) account.get("email");
  }

  @Override
  public String getImageUrl() {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

    if (account == null) {
      return null;
    }

    Map<String, Object> profile = (Map<String, Object>) account.get("profile");

    if (profile == null) {
      return null;
    }

    return (String) profile.get("thumbnail_image_url");
  }
}

package com.lew663.blog.domain.member.oauth2;

import java.util.Map;

public class GoogleOAuth2Info extends OAuth2Info {

  /**
   *  Google response json 예시
   *
   * {
   *    "sub": "식별값",
   *    "name": "name",
   *    "given_name": "given_name",
   *    "picture": "https//lh3.googleusercontent.com/~~",
   *    "email": "email",
   *    "email_verified": true,
   *    "locale": "ko"
   * }
   */
  public GoogleOAuth2Info(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

  @Override
  public String getImageUrl() {
    return (String) attributes.get("picture");
  }
}

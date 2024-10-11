package com.lew663.blog.member.dto.oauth2Info;

import java.util.Map;

public class NaverOAuth2Info extends OAuth2Info{


  /**
   *  네이버 Response JSON 예시
   *
   *  {
   *    "resultcode": "00",
   *    "message": "success",
   *    "response": {
   *       "email": "openapi@naver.com",
   *       "nickname": "OpenAPI",
   *       "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
   *       "age": "40-49",
   *       "gender": "F",
   *       "id": "32742776",
   *       "name": "오픈 API",
   *       "birthday": "10-01"
   *       "mobile": "010-0000-0000"
   *    }
   *  }
   */
  public NaverOAuth2Info(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }
    return (String) response.get("id");
  }

  @Override
  public String getEmail() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }
    return (String) response.get("email");
  }

  @Override
  public String getImageUrl() {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    if (response == null) {
      return null;
    }
    return (String) response.get("profile_image");
  }
}

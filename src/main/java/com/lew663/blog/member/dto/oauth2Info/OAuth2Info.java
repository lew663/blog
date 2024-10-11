package com.lew663.blog.member.dto.oauth2Info;

import java.util.Map;

public abstract class OAuth2Info {

  protected Map<String, Object> attributes;

  public OAuth2Info(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public abstract String getId();

  public abstract String getEmail();

  public abstract String getImageUrl();
}

package com.lew663.blog.util;

import java.security.SecureRandom;

public class PasswordUtil {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
  private static final int PASSWORD_LENGTH = 12; // 원하는 비밀번호 길이
  private static final SecureRandom random = new SecureRandom();

  public static String generateRandomPassword() {
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int randomIndex = random.nextInt(CHARACTERS.length());
      password.append(CHARACTERS.charAt(randomIndex));
    }
    return password.toString();
  }
}

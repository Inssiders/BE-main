package com.inssider.api.common;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountDataTypes.AccountType;
import com.inssider.api.domains.account.AccountDataTypes.RoleType;
import java.net.URI;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;
import lombok.NonNull;

public class Util {

  private Util() {
    // Prevent instantiation
  }

  private static final Random random = new SecureRandom();

  public static final String EMAIL_REGEX =
      "^(?!.*\\.\\.)[a-z0-9](?:[a-z0-9._-]{0,62}[a-z0-9])?@(?:(?!-)[a-z0-9-]{2,}(?<!-)\\.)+[a-z]{2,}$";
  public static final String PASSWORD_REGEX =
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s])\\S{8,64}$";

  public static URI buildAbsoluteUri(String path) {
    final String BASE_URL = "https://api.inssider.com";

    if (path == null || path.isBlank()) {
      path = "/";
    } else if (!path.startsWith("/")) {
      path = java.nio.file.Paths.get("/", path).toString();
    }

    return URI.create(BASE_URL + path);
  }

  public static boolean isValidEmail(@NonNull String email) {
    return email.matches(EMAIL_REGEX);
  }

  public static boolean isValidPassword(@NonNull String password) {
    return password.matches(PASSWORD_REGEX);
  }

  private static String generateRandomString(String charset, int length) {
    return random
        .ints(length, 0, charset.length())
        .mapToObj(charset::charAt)
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
        .toString();
  }

  public static Supplier<String> emailGenerator() {
    final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    Supplier<String> randomWord = () -> generateRandomString(LOWERCASE, 3);
    return () -> {
      String email = randomWord.get() + "@" + randomWord.get() + "." + randomWord.get();
      return isValidEmail(email) ? email : emailGenerator().get();
    };
  }

  public static Supplier<String> passwordGenerator() {
    final String PASSWORD = "abcdefgABCDEFG1234567!@#$%^&*";
    Supplier<String> randomWord = () -> generateRandomString(PASSWORD, 8);
    return () -> {
      String password = randomWord.get() + randomWord.get() + randomWord.get();
      return isValidPassword(password) ? password : passwordGenerator().get();
    };
  }

  public static Supplier<Account> accountGenerator() {
    return () -> {
      String email = emailGenerator().get();
      String password = passwordGenerator().get();
      return Account.builder()
          .accountType(AccountType.PASSWORD)
          .email(email)
          .role(RoleType.USER)
          .password(password)
          .build();
    };
  }

  public static Supplier<String> codeGenerator() {
    final String CODE = "0123456789";
    return () -> generateRandomString(CODE, 6);
  }
}

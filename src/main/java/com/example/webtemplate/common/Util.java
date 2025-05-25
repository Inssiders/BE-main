package com.example.webtemplate.common;

import java.net.URI;

public class Util {

  private Util() {
    // Prevent instantiation
  }

  public static URI buildAbsoluteUri(String path) {
    final String BASE_URL = "https://inssider.oomia.click";

    if (path == null || path.isBlank()) {
      path = "/";
    } else if (!path.startsWith("/")) {
      path = java.nio.file.Paths.get("/", path).toString();
    }

    return URI.create(BASE_URL + path);
  }

  public static String argon2Hash(String password) {
    // var encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    // return encoder.encode(password);

    // simple mock implementation for demonstration
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
    return "argon2id$" + password; // Mock hash for demonstration purposes
  }
}

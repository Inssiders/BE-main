package com.example.webtemplate.account.model;

public enum AccountType {
  LOCAL, GOOGLE, FACEBOOK, GITHUB, TWITTER, LINKEDIN, MICROSOFT, APPLE, OTHER;

  public boolean isLocal() {
    return this == LOCAL;
  }

  public boolean isSocial() {
    return !isLocal();
  }
}

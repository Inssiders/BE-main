package com.example.webtemplate.account.model;

public enum RoleType {
  USER, CONTENT_MANAGER, SUPERADMIN;

  public boolean isUser() {
    return this == USER;
  }

  public boolean isAdmin() {
    return this == SUPERADMIN;
  }
}

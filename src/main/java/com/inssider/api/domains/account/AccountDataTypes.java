package com.inssider.api.domains.account;

public class AccountDataTypes {
  public enum RoleType {
    USER,
    CONTENT_MANAGER,
    SUPERADMIN;

    public boolean isUser() {
      return this == USER;
    }

    public boolean isAdmin() {
      return this != USER;
    }
  }

  public enum RegisterType {
    PASSWORD,
    ADMIN;
  }

  public enum AccountType {
    PASSWORD,
    OTHER;

    public boolean isLocal() {
      return this == PASSWORD;
    }
  }
}

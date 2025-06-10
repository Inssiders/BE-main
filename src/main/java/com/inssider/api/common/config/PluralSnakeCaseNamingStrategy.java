package com.inssider.api.common.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PluralSnakeCaseNamingStrategy implements PhysicalNamingStrategy {

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    String snakeCase = toSnakeCase(name.getText());
    String plural = toPlural(snakeCase);
    return Identifier.toIdentifier(plural);
  }

  private String toSnakeCase(String input) {
    return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }

  private String toPlural(String input) {
    if (input.endsWith("y")) {
      return input.substring(0, input.length() - 1) + "ies";
    }
    return input + "s";
  }

  // 나머지 메서드는 기본 구현 사용
  @Override
  public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
    return name;
  }

  @Override
  public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
    return name;
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
    return name;
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
    String snakeCase = toSnakeCase(name.getText());
    return Identifier.toIdentifier(snakeCase);
  }
}

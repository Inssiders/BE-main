package com.inssider.api.domains;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class DevEnvironmentTests {

  @Value("${spring.jpa.hibernate.ddl-auto}")
  String ddlAuto;

  @Value("${spring.datasource.url}")
  String dbUrl;

  @Value("${spring.datasource.username}")
  String dbUsername;

  @Value("${spring.datasource.password}")
  String dbPassword;

  @Value("${server.port}")
  String serverPort;

  @Test
  void checkActiveProfile(@Autowired Environment env) {
    assertTrue(Arrays.asList(env.getActiveProfiles()).contains("dev"));
  }

  @Test
  void ensuresIdempotency() {
    assertEquals("create-drop", ddlAuto);
    assertEquals("jdbc:postgresql://localhost:5432/dev", dbUrl);
    assertEquals("user", dbUsername);
    assertEquals("user", dbPassword);
    assertEquals("8080", serverPort);
  }
}

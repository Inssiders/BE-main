package com.inssider.api.domains;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebtemplateApplicationTests {

  @Autowired(required = false)
  private BuildProperties buildProperties;

  @Test
  void contextLoads() {
    // build/resources/main/META-INF/build-info.properties
    if (buildProperties == null) {
      return; // buildProperties only created after build, not test
    }

    LocalDate buildDate = buildProperties.getTime().atZone(ZoneOffset.UTC).toLocalDate();
    LocalDate today = LocalDate.now(ZoneOffset.UTC);

    assertEquals(today, buildDate);
    assertEquals("0.0.10-SNAPSHOT", buildProperties.getVersion());
    assertEquals("api", buildProperties.getArtifact());
    assertEquals("api", buildProperties.getName());
    assertEquals("com.inssider", buildProperties.getGroup());
  }
}

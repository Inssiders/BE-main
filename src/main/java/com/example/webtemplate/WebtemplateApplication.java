package com.example.webtemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WebtemplateApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebtemplateApplication.class, args);
  }

}

package com.inssider.api.domains.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class PasswordCodecConfig {

  @Bean
  @Scope
  public PasswordEncoder passwordEncoder() {
    DelegatingPasswordEncoder passwordEncoder =
        (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
    passwordEncoder.setDefaultPasswordEncoderForMatches(
        Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
    return passwordEncoder;
  }
}

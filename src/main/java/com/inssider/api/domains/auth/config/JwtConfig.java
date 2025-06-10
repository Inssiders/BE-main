package com.inssider.api.domains.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

  private RSAKey rsaKey;

  @Bean
  public JwtDecoder jwtDecoder() {
    try {
      return NimbusJwtDecoder.withPublicKey(getRsaKey().toRSAPublicKey()).build();
    } catch (com.nimbusds.jose.JOSEException e) {
      throw new IllegalStateException("Failed to get RSA public key for JWT decoder", e);
    }
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(getRsaKey()));
    return new NimbusJwtEncoder(jwkSource);
  }

  private RSAKey getRsaKey() {
    if (rsaKey == null) {
      rsaKey = generateRSAKey();
    }
    return rsaKey;
  }

  private RSAKey generateRSAKey() {
    KeyPair keyPair = generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }

  private KeyPair generateKeyPair() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
    } catch (Exception e) {
      throw new IllegalStateException("Failed to generate RSA key pair", e);
    }
  }
}

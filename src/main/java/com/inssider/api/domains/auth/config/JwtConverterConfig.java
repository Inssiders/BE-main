package com.inssider.api.domains.auth.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class JwtConverterConfig {

  @Bean
  public Converter<Jwt, AbstractAuthenticationToken> customJwtAuthenticationConverter() {
    return new CustomAuthenticationConverter();
  }

  class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final String ROLES_CLAIM = "roles";
    private static final String SCOPE_CLAIM = "scope";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String SCOPE_PREFIX = "SCOPE_";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
      // String principalClaimValue = jwt.getSubject(); // "sub" 클레임 사용

      List<GrantedAuthority> authorities = new ArrayList<>();

      // roles 클레임 처리
      List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);
      if (roles != null) {
        roles.forEach(
            role -> authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase())));
      }

      // scope 클레임 처리
      String scopeClaim = jwt.getClaimAsString(SCOPE_CLAIM);
      if (scopeClaim != null && !scopeClaim.isBlank()) {
        String[] scopes = scopeClaim.split("\\s+");
        for (String scope : scopes) {
          authorities.add(new SimpleGrantedAuthority(SCOPE_PREFIX + scope));
        }
      }

      return new JwtAuthenticationToken(jwt, authorities);
    }
  }
}

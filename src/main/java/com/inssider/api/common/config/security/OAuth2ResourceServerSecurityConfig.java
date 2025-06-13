package com.inssider.api.common.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class OAuth2ResourceServerSecurityConfig {

  private final Converter<Jwt, AbstractAuthenticationToken> customConverter;

  @Bean
  @Order(1)
  public SecurityFilterChain publicPathsChain(HttpSecurity http) throws Exception {
    return http.securityMatchers(
            matchers ->
                matchers
                    .requestMatchers(getSystemPublicPaths())
                    .requestMatchers(HttpMethod.POST, publicPostPaths())
                    .requestMatchers(HttpMethod.GET, publicGetPaths()))
        .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain protectedApiChain(HttpSecurity http) throws Exception {
    return http.securityMatchers(matchers -> matchers.requestMatchers("/api/**"))
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customConverter)))
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  /** 시스템 레벨 공개 경로 반환 */
  private String[] getSystemPublicPaths() {
    return new String[] {"/", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health"};
  }

  private String[] publicPostPaths() {
    return new String[] {"/api/auth/email/challenge", "/api/auth/email/verify", "/api/auth/token"};
  }

  private String[] publicGetPaths() {
    return new String[] {
      "/api/accounts",
      "/api/accounts/{email}",
      "/api/profiles",
      "/api/profiles/index",
      "/api/profiles/{id}",
      "/api/posts",
      "/api/posts/{id}",
      "/api/memes/sitemap",
      "/api/comments/{id}",
      "/api/categories"
    };
  }
}

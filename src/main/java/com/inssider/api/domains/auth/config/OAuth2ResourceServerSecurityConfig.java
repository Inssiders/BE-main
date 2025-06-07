package com.inssider.api.domains.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OAuth2ResourceServerSecurityConfig {

  private final Converter<Jwt, AbstractAuthenticationToken> customConverter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(this::configureAuthorization)
        .oauth2ResourceServer(this::configureOAuth2ResourceServer)
        .build();
  }

  private void configureAuthorization(
      AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
          authz) {
    authz
        // 1. 시스템 공개 경로
        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/health")
        .permitAll()

        // 2. 인증 관련 공개 API
        .requestMatchers(
            HttpMethod.POST,
            "/api/auth/email/challenge",
            "/api/auth/email/verify",
            "/api/auth/token")
        .permitAll()

        // 3. 읽기 전용 공개 API
        .requestMatchers(
            HttpMethod.GET,
            "/api/profiles",
            "/api/profiles/index",
            "/api/profiles/{id}",
            "/api/posts",
            "/api/posts/{id}",
            "/api/memes/sitemap",
            "/api/comments/{id}")
        .permitAll()

        // 4. 나머지 모든 API는 인증 필요
        .requestMatchers("/api/**")
        .authenticated()

        // 5. 기타 요청 허용
        .anyRequest()
        .permitAll();
  }

  private void configureOAuth2ResourceServer(
      org.springframework.security.config.annotation.web.configurers.oauth2.server.resource
                  .OAuth2ResourceServerConfigurer<
              HttpSecurity>
          oauth2) {
    oauth2.jwt(Customizer.withDefaults()); // use JwtDecoder bean
    oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customConverter));
  }
}

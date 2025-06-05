package com.inssider.api.domains.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtDecoder jwtDecoder;

  public SecurityConfig(JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authz ->
                authz
                    // Swagger UI 관련 경로들 허용
                    .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**")
                    .permitAll()

                    // 인증 관련 API 허용 (향후 부분 적용 필요)
                    .requestMatchers("/api/auth/**")
                    .permitAll()

                    // 임시로 모든 엔드포인트 허용
                    .anyRequest()
                    .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));

    return http.build();
  }
}

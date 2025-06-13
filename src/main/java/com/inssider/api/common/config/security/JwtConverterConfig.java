package com.inssider.api.common.config.security;

import com.inssider.api.common.model.AccountJwtAuthenticationToken;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@RequiredArgsConstructor
class JwtConverterConfig {

  private final AccountService accountService;

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
      Set<GrantedAuthority> authorities = getAuthoritiesFrom(jwt);

      if ("single_access".equals(jwt.getClaimAsString("type"))) {
        return new JwtAuthenticationToken(jwt, authorities);
      }
      Long accountId = Long.parseLong(jwt.getSubject());
      Account account =
          accountService
              .findById(accountId)
              .orElseThrow(() -> new BadCredentialsException("Account not found: " + accountId));

      validateTokenType(jwt, account);

      authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + account.getRole().name()));

      return new AccountJwtAuthenticationToken(
          new JwtAuthenticationToken(jwt, authorities), account);
    }

    private Set<GrantedAuthority> getAuthoritiesFrom(Jwt jwt) {
      Set<GrantedAuthority> authorities = new HashSet<>();

      // roles 클레임 처리
      Optional.ofNullable(jwt.getClaimAsStringList(ROLES_CLAIM))
          .ifPresent(
              roles ->
                  roles.stream()
                      .map(String::trim)
                      .filter(role -> !role.isEmpty())
                      .map(role -> ROLE_PREFIX + role.toUpperCase())
                      .map(role -> new SimpleGrantedAuthority(role))
                      .forEach(authorities::add));

      // scope 클레임 처리
      Optional.ofNullable(jwt.getClaimAsString(SCOPE_CLAIM))
          .map(String::trim)
          .filter(scope -> !scope.isBlank())
          .ifPresent(
              trimmedScope ->
                  Arrays.stream(trimmedScope.split("\\s+"))
                      .filter(scope -> !scope.isBlank())
                      .map(scope -> new SimpleGrantedAuthority(SCOPE_PREFIX + scope.trim()))
                      .forEach(authorities::add));

      return authorities;
    }

    private void validateTokenType(Jwt jwt, Account account) throws BadCredentialsException {
      if (account.isDeleted()) {
        throw new BadCredentialsException("Account is deleted");
      }

      String tokenType = jwt.getClaimAsString("type");
      if ("access".equals(tokenType)) {
        return; // 정상적인 access 토큰
      }

      // refresh_token가 아닌 경우, RefreshToken 체크
      if (!"single_access".equals(tokenType) && account.getRefreshToken() == null) {
        throw new BadCredentialsException("Invalid token state");
      }

      validateTokenExpiration(jwt);
    }

    private void validateTokenExpiration(Jwt jwt) throws BadCredentialsException {
      Instant now = Instant.now();

      // 발급 시간 검증 (토큰이 미래에 발급되지 않았는지)
      Instant issuedAt = jwt.getIssuedAt();
      if (issuedAt != null && issuedAt.isAfter(now)) {
        throw new BadCredentialsException("Token issued in the future: " + issuedAt);
      }

      // 만료 시간 검증
      Instant expiresAt = jwt.getExpiresAt();
      if (expiresAt != null && expiresAt.isBefore(now)) {
        throw new BadCredentialsException("Token has expired: " + expiresAt);
      }

      // 토큰 활성화 시간 검증 (nbf 클레임이 있다면)
      Instant notBefore = jwt.getNotBefore();
      if (notBefore != null && notBefore.isAfter(now)) {
        throw new BadCredentialsException("Token not yet valid: " + notBefore);
      }
    }
  }
}

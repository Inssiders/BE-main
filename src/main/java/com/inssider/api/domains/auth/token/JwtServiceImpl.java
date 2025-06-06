package com.inssider.api.domains.auth.token;

import static com.inssider.api.domains.auth.AuthDataTypes.GrantType.AUTHORIZATION_CODE;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
import com.inssider.api.domains.auth.code.AuthorizationCode;
import com.inssider.api.domains.auth.code.AuthorizationCodeService;
import com.inssider.api.domains.auth.token.refresh.RefreshTokenService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final AuthorizationCodeService authorizationCodeService;
  private final RefreshTokenService refreshTokenService;
  private final AccountService accountService;

  public JwtServiceImpl(
      JwtEncoder jwtEncoder,
      JwtDecoder jwtDecoder,
      AuthorizationCodeService authorizationCodeService,
      RefreshTokenService refreshTokenService,
      AccountService accountService) {
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
    this.authorizationCodeService = authorizationCodeService;
    this.refreshTokenService = refreshTokenService;
    this.accountService = accountService;
  }

  // [ ] Set configuration properties for token expiration times
  @Value("${jwt.access-token.expiration:3600}")
  private long accessTokenExpiration;

  @Value("${jwt.refresh-token.expiration:604800}")
  private long refreshTokenExpiration;

  @Override
  public void revokeRefreshToken(Long accountId) {
    refreshTokenService.deleteById(accountId);
  }

  @Override
  public TokenResponse permitAccessTokenByAuthorizationCode(UUID authorizationCode) {
    AuthorizationCode authCodeEntity =
        authorizationCodeService.findById(authorizationCode).orElseThrow();

    Account account =
        accountService
            .findByEmail(authCodeEntity.getEmail())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Account not found for email: " + authCodeEntity.getEmail()));
    authorizationCodeService.deleteById(authCodeEntity.getId());
    return generateTokenResponse(AUTHORIZATION_CODE, account);
  }

  @Override
  public TokenResponse generateTokenResponse(GrantType grantType, Account account) {
    String accessToken = generateToken(account.getId(), accessTokenExpiration, "access");
    String refreshToken = null;

    if (grantType != AUTHORIZATION_CODE) {
      refreshToken = generateToken(account.getId(), refreshTokenExpiration, "refresh");
    }

    assert accessToken != null;
    return new TokenResponse(accessToken, refreshToken, "Bearer", accessTokenExpiration);
  }

  private String generateToken(Long accountId, long expiration, String tokenType) {
    Instant now = Instant.now();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(String.valueOf(accountId))
            .issuer("inssider-api")
            .issuedAt(now)
            .audience(List.of("inssider-api", "inssider-app"))
            .expiresAt(now.plus(expiration, ChronoUnit.SECONDS))
            .claim("type", tokenType)
            .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  @Override
  public Long getAccountIdFromAccessToken(String accessToken) {
    return Long.valueOf(jwtDecoder.decode(accessToken).getSubject());
  }

  @Override
  public Long countAuthorizationCodes() {
    return authorizationCodeService.count();
  }
}

package com.inssider.api.domains.auth.token;

import static com.inssider.api.domains.auth.AuthDataTypes.GrantType.AUTHORIZATION_CODE;
import static com.inssider.api.domains.auth.AuthDataTypes.GrantType.PASSWORD;
import static com.inssider.api.domains.auth.AuthDataTypes.GrantType.REFRESH_TOKEN;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountAuthenticator;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthTokenServiceImpl implements AuthTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  // common
  private final JwtEncoder jwtEncoder;

  // account-related
  private final AccountAuthenticator authenticator;

  /** Access Token의 만료 시간 (초 단위) 기본값: 3600초 (1시간) */
  @Value("${jwt.access-token.expiration:3600}")
  private long accessTokenExpiration;

  /** Refresh Token의 만료 시간 (초 단위) 기본값: 604800초 (7일) */
  @Value("${jwt.refresh-token.expiration:604800}")
  private long refreshTokenExpiration;

  /**
   * 이메일과 비밀번호를 사용하여 토큰을 발급합니다.
   *
   * @param email 사용자 이메일 주소
   * @param rawPassword 평문 비밀번호
   * @return 생성된 Access Token과 Refresh Token을 포함한 응답
   * @throws IllegalArgumentException 인증 정보가 올바르지 않은 경우
   * @throws NoSuchElementException 사용자를 찾을 수 없는 경우
   */
  @Override
  public AuthTokenResponse permitTokensByPassword(String email, String rawPassword) {
    var account = authenticator.authenticate(email, rawPassword);
    return generateTokenResponse(PASSWORD, account);
  }

  /**
   * Refresh Token을 사용하여 새로운 토큰을 발급합니다.
   *
   * @param refreshToken 유효한 Refresh Token
   * @return 새로 생성된 Access Token과 Refresh Token을 포함한 응답
   * @throws IllegalArgumentException Refresh Token이 유효하지 않은 경우
   * @throws NoSuchElementException 토큰과 연관된 계정을 찾을 수 없는 경우
   */
  @Override
  public AuthTokenResponse permitTokensByRefreshToken(String refreshToken) {
    Account account = authenticator.getAccountFromToken(refreshToken);

    // 저장된 토큰과 일치하는지 검증
    var storedRefreshToken = account.getRefreshToken().getToken();
    if (!refreshToken.equals(storedRefreshToken)) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    return generateTokenResponse(REFRESH_TOKEN, account);
  }

  /**
   * 인증 코드를 사용하여 토큰을 발급합니다.
   *
   * @param authorizationCode 유효한 인증 코드 UUID
   * @return 생성된 Access Token을 포함한 응답 (일회성이므로 Refresh Token 미포함)
   * @throws NoSuchElementException 인증 코드를 찾을 수 없는 경우
   * @throws IllegalArgumentException 인증 코드가 유효하지 않은 경우
   */
  @Override
  public AuthTokenResponse permitTokensByAuthorizationCode(UUID authorizationCode) {
    // 비밀번호 찾기를 위해 인증 코드가 사용되었다면 계정 정보를 찾을 수 있지만
    // 초기에 회원가입을 진행을 위해 사용되었다면 이메일만 존재하는 임시 계정이 반환됩니다.
    Account account = authenticator.redeemAuthorizationCode(authorizationCode);
    return generateTokenResponse(AUTHORIZATION_CODE, account);
  }

  /**
   * 계정의 Refresh Token을 무효화합니다.
   *
   * @param account 토큰을 무효화할 계정
   * @throws NullPointerException account가 null인 경우
   * @throws IllegalArgumentException 계정에 Refresh Token이 없는 경우
   */
  @Override
  public void revokeRefreshToken(Account account)
      throws NullPointerException, IllegalArgumentException {
    RefreshToken refreshToken = account.getRefreshToken();
    refreshTokenRepository.delete(refreshToken);
  }

  /**
   * 토큰 문자열을 사용하여 해당 Refresh Token을 무효화합니다.
   *
   * @param token 무효화할 Refresh Token 문자열
   * @throws NoSuchElementException 토큰과 연관된 계정을 찾을 수 없는 경우
   */
  @Override
  public void revokeRefreshToken(String token)
      throws NullPointerException, IllegalArgumentException {
    Account account = authenticator.getAccountFromToken(token);
    revokeRefreshToken(account);
  }

  /**
   * Grant Type에 따라 적절한 토큰 응답을 생성합니다.
   *
   * @param grantType 토큰 발급 방식
   * @param account 토큰을 발급받을 계정
   * @return Grant Type에 맞는 토큰 응답
   */
  AuthTokenResponse generateTokenResponse(GrantType grantType, Account account) {
    return switch (grantType) {
      case AUTHORIZATION_CODE -> {
        String accessToken = generateSingleAccessToken(account.getEmail(), accessTokenExpiration);
        yield new AuthTokenResponse(accessToken, null, "Bearer", accessTokenExpiration);
      }
      case PASSWORD, REFRESH_TOKEN -> {
        // 1. generate new tokens
        String accessToken = generateToken(account, accessTokenExpiration, "access");
        String refreshToken = generateToken(account, refreshTokenExpiration, "refresh");

        // 2. create or update refresh token
        createOrUpdateRefreshToken(account, refreshToken);

        yield new AuthTokenResponse(accessToken, refreshToken, "Bearer", accessTokenExpiration);
      }
    };
  }

  /**
   * 계정의 Refresh Token을 생성하거나 업데이트합니다.
   *
   * <p>기존 Refresh Token이 있으면 새 토큰으로 업데이트하고, 없으면 새로운 Refresh Token 엔티티를 생성합니다.
   *
   * @param account Refresh Token을 연결할 계정
   * @param refreshToken 새로 생성된 Refresh Token 문자열
   */
  void createOrUpdateRefreshToken(Account account, String refreshToken) {
    RefreshToken entity =
        Optional.ofNullable(account.getRefreshToken())
            .map(
                existing -> {
                  existing.setToken(refreshToken);
                  return existing;
                })
            .orElse(new RefreshToken(account, refreshToken));

    refreshTokenRepository.save(entity);
  }

  /**
   * JWT 토큰을 생성합니다.
   *
   * <ul>
   *   <li>issuer: api.inssider.com
   *   <li>subject: 계정 ID
   *   <li>audience: inssider-app
   *   <li>type: 토큰 타입 (access, refresh, single_access)
   *   <li>jti: 고유 토큰 ID (UUID)
   * </ul>
   *
   * @param account 토큰 주체가 될 계정
   * @param expiration 토큰 만료 시간 (초 단위)
   * @param tokenType 토큰 타입 ("access", "refresh", "single_access")
   * @return 생성된 JWT 토큰 문자열
   */
  String generateToken(Account account, long expiration, String tokenType) {
    Instant now = Instant.now();
    Long accountId = account.getId();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("api.inssider.com")
            .issuedAt(now)
            .audience(List.of("inssider-app"))
            .subject(String.valueOf(accountId))
            .expiresAt(now.plus(expiration, ChronoUnit.SECONDS))
            .claim("type", tokenType)
            .id(UUID.randomUUID().toString())
            .build();

    var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    return token;
  }

  String generateSingleAccessToken(String email, long expiration) {
    Instant now = Instant.now();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("api.inssider.com")
            .issuedAt(now)
            .audience(List.of("inssider-app"))
            .subject(email) // 이메일을 subject로 사용
            .expiresAt(now.plus(expiration, ChronoUnit.SECONDS))
            .claim("type", "single_access")
            .id(UUID.randomUUID().toString())
            .build();

    var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    return token;
  }
}

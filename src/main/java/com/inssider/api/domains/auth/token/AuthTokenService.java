package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthResponsesDto.AuthTokenResponse;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface AuthTokenService {
  AuthTokenResponse permitTokensByPassword(String email, String rawPassword);

  AuthTokenResponse permitTokensByRefreshToken(String refreshToken);

  AuthTokenResponse permitTokensByAuthorizationCode(UUID uuid);

  /**
   * 계정의 Refresh Token을 무효화합니다.
   *
   * @param account 토큰을 무효화할 계정
   * @throws NullPointerException account가 null인 경우
   * @throws IllegalArgumentException 계정에 Refresh Token이 없는 경우
   */
  void revokeRefreshToken(Account account) throws NullPointerException, IllegalArgumentException;

  /**
   * 토큰 문자열을 사용하여 해당 Refresh Token을 무효화합니다.
   *
   * @param token 무효화할 Refresh Token 문자열
   * @throws NoSuchElementException 토큰과 연관된 계정을 찾을 수 없는 경우
   */
  void revokeRefreshToken(String token) throws NullPointerException, IllegalArgumentException;
}

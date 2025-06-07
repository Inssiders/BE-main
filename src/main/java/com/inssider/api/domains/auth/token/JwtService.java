package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
import java.util.UUID;

public interface JwtService {

  void revokeRefreshToken(Long accountId);

  TokenResponse generateTokenResponse(GrantType grantType, Long accountId);

  Long getAccountIdFromAccessToken(String accessToken);

  Long countAuthorizationCodes();

  TokenResponse permitAccessTokenByAuthorizationCode(UUID authorizationCode);

  TokenResponse permitTokensByPassword(String email, String rawPassword);
}

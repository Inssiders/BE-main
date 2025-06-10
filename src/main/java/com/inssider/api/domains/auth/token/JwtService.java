package com.inssider.api.domains.auth.token;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.auth.AuthDataTypes.GrantType;
import com.inssider.api.domains.auth.AuthResponsesDto.TokenResponse;
import java.util.UUID;

public interface JwtService {

  void revokeRefreshToken(Long accountId);

  TokenResponse permitAccessTokenByAuthorizationCode(UUID authorizationCode);

  TokenResponse generateTokenResponse(GrantType grantType, Account account);

  Long getAccountIdFromAccessToken(String accessToken);

  Long countAuthorizationCodes();
}

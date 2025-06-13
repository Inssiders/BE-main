package com.inssider.api.domains.account;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.domains.account.AccountRequestsDto.PatchAccountPasswordRequest;
import com.inssider.api.domains.account.AccountRequestsDto.PostAccountRequest;
import com.inssider.api.domains.account.AccountResponsesDto.PatchAccountMePasswordResponse;
import com.inssider.api.domains.account.AccountResponsesDto.PostAccountResponse;
import com.inssider.api.domains.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
class AccountController {

  private final AccountService service;
  private final AuthService authService;

  @RequestMapping(value = "/{email}", method = RequestMethod.GET)
  ResponseEntity<ResponseWrapper<Void>> isAccountAvailable(@PathVariable("email") String email) {
    int status = service.existsByEmail(email) ? 204 : 404;
    return BaseResponse.of(status, null);
  }

  @PostMapping
  // @SecurityRequirement(name = "AccessToken")
  ResponseEntity<ResponseWrapper<PostAccountResponse>> register(
      // single_access token은 JWT.sub를 직접 활용해야 함
      @AuthenticationPrincipal Jwt jwt, @RequestBody PostAccountRequest reqBody) {
    var email = jwt.getSubject();
    if (!email.equals(reqBody.email())) {
      throw new IllegalArgumentException("Email in request body must match authenticated email");
    }
    var data = service.register(reqBody.registerType(), reqBody.email(), reqBody.password());
    return BaseResponse.of(201, new PostAccountResponse(data.getEmail(), data.getCreatedAt()));
  }

  // 회원 탈퇴
  @DeleteMapping("/me")
  ResponseEntity<ResponseWrapper<Void>> deleteAccount(@AuthenticationPrincipal Account account) {
    service.softDelete(account.getId());
    return BaseResponse.of(200, null);
  }

  @PatchMapping("/me/password")
  ResponseEntity<ResponseWrapper<PatchAccountMePasswordResponse>> changePassword(
      @AuthenticationPrincipal Account account, @RequestBody PatchAccountPasswordRequest reqBody) {
    var response = service.patchAccountPassword(account.getId(), reqBody.password());
    authService.revokeRefreshToken(account);
    return BaseResponse.of(200, new PatchAccountMePasswordResponse(response.getUpdatedAt()));
  }
}

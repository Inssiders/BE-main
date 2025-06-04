package com.inssider.api.domains.account;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.domains.account.AccountRequestsDto.ChangePasswordRequestDto;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import com.inssider.api.domains.account.AccountResponsesDto.AccountCreated;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final AccountService service;

  AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  ResponseEntity<ResponseWrapper<AccountCreated>> register(RegisterRequestDto reqBody) {
    var data = service.register(reqBody.registerType(), reqBody.email(), reqBody.password());
    return BaseResponse.of(201, new AccountCreated(data.getEmail(), new Date()));
  }

  @DeleteMapping("/me")
  ResponseEntity<ResponseWrapper<String>> signOut() {
    // TODO: Implement security context to get current user's ID
    // For now, returning a placeholder response
    return BaseResponse.of(200, "Sign out successful");
  }

  @PatchMapping("/me/password")
  ResponseEntity<ResponseWrapper<Account>> changePassword(
      @RequestBody ChangePasswordRequestDto reqBody) {
    // [ ] `id` will be removed after implementing security context
    return BaseResponse.of(200, service.patchAccountPassword(reqBody.id(), reqBody.password()));
  }
}

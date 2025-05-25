package com.example.webtemplate.account;

import java.util.Date;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webtemplate.account.AccountRequestsDto.Register;
import com.example.webtemplate.account.AccountResponsesDto.AccountCreated;
import com.example.webtemplate.common.response.StandardResponse;
import com.example.webtemplate.common.response.StandardResponse.ResponseWrapper;

@RestController
@RequestMapping("/accounts")
class AccountController {

  private final AccountService service;

  AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping
  ResponseEntity<ResponseWrapper<AccountCreated>> register(@RequestBody Register reqBody) {
    var data = service.register(reqBody.registerType(), reqBody.email(), reqBody.password());
    return StandardResponse.of(201, new AccountCreated(data.getEmail(), new Date()));
  }

  @DeleteMapping("/me")
  ResponseEntity<ResponseWrapper<?>> signOut() {
    // Assuming the current user's ID is given by a security context
    throw new NotImplementedException();
  }

  @PatchMapping("/me/password")
  ResponseEntity<ResponseWrapper<Account>> changePassword(
      @RequestAttribute("id") Long id,
      @RequestAttribute("password") String password) {
    // [ ] use a security context to get the current user's ID
    return StandardResponse.of(200, service.patchAccountPassword(id, password));
  }
}

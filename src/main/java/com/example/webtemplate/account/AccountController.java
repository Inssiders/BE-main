package com.example.webtemplate.account;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AccountController {

  private final AccountService service;

  AccountController(AccountService service) {
    this.service = service;
  }

  @PostMapping("/accounts")
  EntityModel<Account> register(@RequestBody Account newAccount) {
    throw new NotImplementedException();
  }

  @DeleteMapping("/accounts/me")
  EntityModel<Account> signOut() {
    // Assuming the current user's ID is given by a security context
    throw new NotImplementedException();
  }

  @PatchMapping("/accounts/me/password")
  EntityModel<Account> changePassword(@RequestBody Account reqBody) {
    // Assuming the current user's ID is given by a security context
    throw new NotImplementedException();
  }

  @GetMapping("/accounts/index")
  List<Integer> accountIndex() {
    throw new NotImplementedException();
  }
}

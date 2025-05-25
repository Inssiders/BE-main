package com.example.webtemplate.account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.webtemplate.account.dto.AccountPatchPasswordResponse;

@RestController
class AccountController {

  private final AccountService service;

  AccountController(AccountService service) {
    this.service = service;
  }

  @GetMapping("/accounts")
  CollectionModel<EntityModel<Account>> all() {

    List<EntityModel<Account>> accounts = service.getAllAccounts().stream()
        .map(employee -> EntityModel.of(employee,
            linkTo(methodOn(AccountController.class).one(employee.getId())).withSelfRel(),
            linkTo(methodOn(AccountController.class).all()).withRel("accounts")))
        .toList();

    return CollectionModel.of(accounts,
        linkTo(methodOn(AccountController.class).all()).withSelfRel());
  }

  @PostMapping("/accounts")
  Account newEmployee(@RequestBody Account newEmployee) {
    return service.createAccount(newEmployee);
  }

  @GetMapping("/accounts/{id}")
  EntityModel<Account> one(@PathVariable("id") Long id) {
    Account employee = service.findById(id);
    return EntityModel.of(employee, linkTo(methodOn(AccountController.class).one(id)).withSelfRel(),
        linkTo(methodOn(AccountController.class).all()).withRel("accounts"));
  }

  @PatchMapping("/accounts/{id}/password")
  AccountPatchPasswordResponse patchPassword(@PathVariable("id") Long id,
      @RequestBody Account reqBody) {
    service.patchAccountPassword(id, reqBody.getPassword());
    return new AccountPatchPasswordResponse(new java.util.Date());
  }

  @DeleteMapping("/accounts/{id}")
  void deleteEmployee(@PathVariable("id") Long id) {
    service.deleteById(id);
  }
}

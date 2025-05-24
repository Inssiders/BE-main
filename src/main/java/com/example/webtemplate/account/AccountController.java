package com.example.webtemplate.account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AccountController {

	private final AccountRepository repository;

	AccountController(AccountRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/accounts")
	CollectionModel<EntityModel<Account>> all() {

		List<EntityModel<Account>> accounts = repository.findAll().stream()
				.map(employee -> EntityModel.of(employee,
						linkTo(methodOn(AccountController.class).one(employee.getId())).withSelfRel(),
						linkTo(methodOn(AccountController.class).all()).withRel("accounts")))
				.toList();

		return CollectionModel.of(accounts, linkTo(methodOn(AccountController.class).all()).withSelfRel());
	}

	@PostMapping("/accounts")
	Account newEmployee(@RequestBody Account newEmployee) {
		return repository.save(newEmployee);
	}

	@GetMapping("/accounts/{id}")
	EntityModel<Account> one(@PathVariable("id") Long id) {
		Account employee = repository.findById(id).orElseThrow();
		return EntityModel.of(employee,
				linkTo(methodOn(AccountController.class).one(id)).withSelfRel(),
				linkTo(methodOn(AccountController.class).all()).withRel("accounts"));
	}

	@PutMapping("/accounts/{id}")
	Account replaceEmployee(@RequestBody Account newEmployee, @PathVariable("id") Long id) {
		return repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() -> repository.save(newEmployee));
	}

	@DeleteMapping("/accounts/{id}")
	void deleteEmployee(@PathVariable("id") Long id) {
		repository.deleteById(id);
	}
}
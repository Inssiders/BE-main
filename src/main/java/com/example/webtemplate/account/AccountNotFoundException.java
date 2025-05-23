package com.example.webtemplate.account;

class AccountNotFoundException extends RuntimeException {

    AccountNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}
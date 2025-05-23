package com.example.webtemplate;

class AccountNotFoundException extends RuntimeException {

    AccountNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}
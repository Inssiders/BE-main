package com.example.webtemplate.account;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import com.example.webtemplate.account.AccountDataTypes.RegisterType;

public interface AccountService {
    Account register(RegisterType registerType, String email, String password) throws IllegalArgumentException;

    Account patchAccountPassword(Long id, String newPassword) throws NoSuchElementException;

    long count();

    LocalDateTime softDelete(Long id) throws NoSuchElementException;
}
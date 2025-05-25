package com.example.webtemplate.account;

import com.example.webtemplate.account.AccountDataTypes.RegisterType;

public class AccountRequestsDto {
    public record Register(RegisterType registerType, String email, String password) {
    }
}

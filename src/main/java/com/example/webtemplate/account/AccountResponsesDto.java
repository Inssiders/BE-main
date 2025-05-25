package com.example.webtemplate.account;

import java.util.Date;

public class AccountResponsesDto {
    public record PatchPassword(Date updatedAt) {
    }

    public record AccountCreated(String email, Date createdAt) {
    }
}

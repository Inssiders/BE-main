package com.example.webtemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class InitialEntityStateTest {

    @Autowired
    private AccountRepository repository;

    @Test
    void shouldHaveInitialAccountData() {
        var accounts = repository.findAll();
        assertEquals(2, accounts.size());
    }
}
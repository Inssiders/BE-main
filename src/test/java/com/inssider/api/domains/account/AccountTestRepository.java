package com.inssider.api.domains.account;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;

@TestComponent
@Primary
public interface AccountTestRepository extends AccountRepository {}

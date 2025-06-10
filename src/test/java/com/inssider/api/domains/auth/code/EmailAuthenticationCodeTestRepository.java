package com.inssider.api.domains.auth.code;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;

@TestComponent
@Primary
public interface EmailAuthenticationCodeTestRepository extends EmailAuthenticationCodeRepository {}

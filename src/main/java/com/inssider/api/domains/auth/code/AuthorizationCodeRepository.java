package com.inssider.api.domains.auth.code;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, UUID> {}

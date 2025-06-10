package com.inssider.api.domains.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {}

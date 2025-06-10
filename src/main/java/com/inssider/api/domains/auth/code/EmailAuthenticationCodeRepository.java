package com.inssider.api.domains.auth.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EmailAuthenticationCodeRepository extends JpaRepository<EmailAuthenticationCode, String> {

  default EmailAuthenticationCode save(String email) {
    return save(new EmailAuthenticationCode(email));
  }
}

package com.inssider.api.domains.auth.code.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EmailAuthCodeRepository extends JpaRepository<EmailAuthCode, String> {

  default EmailAuthCode save(String email) {
    return save(new EmailAuthCode(email));
  }
}

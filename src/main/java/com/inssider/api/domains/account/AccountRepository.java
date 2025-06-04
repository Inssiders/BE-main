package com.inssider.api.domains.account;

import com.inssider.api.common.repository.SoftDeleteRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends SoftDeleteRepository<Account, Long> {

  boolean existsByEmail(String email);

  Optional<Account> findByEmail(String email);
}

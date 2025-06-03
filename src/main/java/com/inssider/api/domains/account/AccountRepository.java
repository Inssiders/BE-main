package com.inssider.api.domains.account;

import com.inssider.api.common.repository.SoftDeleteRepository;

interface AccountRepository extends SoftDeleteRepository<Account, Long> {

  boolean existsByEmail(String email);
}

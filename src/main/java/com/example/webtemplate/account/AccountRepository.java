package com.example.webtemplate.account;

import com.example.webtemplate.common.repository.SoftDeleteRepository;

interface AccountRepository extends SoftDeleteRepository<Account, Long> {

}

package com.inssider.api.domains.auth.code;

import com.inssider.api.common.service.BaseCRUDService;
import java.util.UUID;

public interface AuthorizationCodeService
    extends BaseCRUDService<AuthorizationCode, UUID, AuthorizationCodeRepository> {}

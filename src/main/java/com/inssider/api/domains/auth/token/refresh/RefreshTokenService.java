package com.inssider.api.domains.auth.token.refresh;

import com.inssider.api.common.service.BaseCRUDService;

/** RefreshToken 전용 서비스 인터페이스 기본 CRUD 외 추가 기능이 필요하면 여기에 정의 */
public interface RefreshTokenService
    extends BaseCRUDService<RefreshToken, Long, RefreshTokenRepository> {
  // 필요시 RefreshToken 전용 메서드 추가
}

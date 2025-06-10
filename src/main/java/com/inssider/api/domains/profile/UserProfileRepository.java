package com.inssider.api.domains.profile;

import com.inssider.api.common.repository.SoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface UserProfileRepository extends SoftDeleteRepository<UserProfile, Long> {
  Page<UserProfile> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
}

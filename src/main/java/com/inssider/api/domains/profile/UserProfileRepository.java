package com.inssider.api.domains.profile;

import com.inssider.api.common.repository.SoftDeleteRepository;

interface UserProfileRepository extends SoftDeleteRepository<UserProfile, Long> {}

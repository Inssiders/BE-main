package com.inssider.api.domains.profile;

import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PatchProfileMeResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserProfileService {

  List<Long> getAllUserProfileIds();

  long count();

  GetProfileResponse findUserProfileById(Long id);

  PatchProfileMeResponse updateUserProfile(
      Long id,
      Optional<String> nickname,
      Optional<String> profileUrl,
      Optional<String> bio,
      Optional<Boolean> accountVisible,
      Optional<Boolean> followerVisible);

  QueryResponse<GetProfileResponse> findUserProfilesByNickname(String nickname, Pageable pageable);
}

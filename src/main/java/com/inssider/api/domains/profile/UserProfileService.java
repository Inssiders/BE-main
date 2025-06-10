package com.inssider.api.domains.profile;

import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UpdateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UserProfileDto;
import java.util.List;
import java.util.Optional;

public interface UserProfileService {

  List<Long> getAllUserProfileIds();

  long count();

  UserProfileDto findUserProfileById(Long id);

  UpdateProfileResponse updateUserProfile(
      Long id,
      Optional<String> nickname,
      Optional<String> profileUrl,
      Optional<String> bio,
      Optional<Boolean> accountVisible,
      Optional<Boolean> followerVisible);

  QueryResponse<UserProfileDto> findUserProfilesByNickname(
      String nickname, String sort, int limit, int page);
}

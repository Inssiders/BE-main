package com.inssider.api.domains.profile;

import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PatchProfileMeResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserProfileServiceImpl implements UserProfileService {
  private final UserProfileRepository repository;

  @Override
  public List<Long> getAllUserProfileIds() {
    return repository.findAll().stream().map(UserProfile::getId).filter(Objects::nonNull).toList();
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public GetProfileResponse findUserProfileById(Long id) {
    UserProfile entity = repository.findById(id).orElseThrow();
    return entity.convertToDto();
  }

  @Override
  public PatchProfileMeResponse updateUserProfile(
      Long id,
      Optional<String> nickname,
      Optional<String> profileUrl,
      Optional<String> bio,
      Optional<Boolean> accountVisible,
      Optional<Boolean> followerVisible) {

    UserProfile entity = repository.findById(id).orElseThrow();

    nickname.ifPresent(entity::setNickname);
    profileUrl.ifPresent(entity::setProfileUrl);
    bio.ifPresent(entity::setBio);
    accountVisible.ifPresent(entity::setAccountVisible);
    followerVisible.ifPresent(entity::setFollowerVisible);

    entity = repository.save(entity);

    return new PatchProfileMeResponse(findUserProfileById(id), entity.getUpdatedAt());
  }

  @Override
  public QueryResponse<GetProfileResponse> findUserProfilesByNickname(
      String nickname, Pageable pageable) {
    Page<UserProfile> userProfilePage;

    if (nickname != null && !nickname.trim().isEmpty()) {
      userProfilePage = repository.findByNicknameContainingIgnoreCase(nickname, pageable);
    } else {
      userProfilePage = repository.findAll(pageable);
    }

    Page<GetProfileResponse> userProfileDtoPage = userProfilePage.map(UserProfile::convertToDto);

    return QueryResponse.of(
        userProfileDtoPage.getContent(), userProfileDtoPage, pageable.getPageSize());
  }
}

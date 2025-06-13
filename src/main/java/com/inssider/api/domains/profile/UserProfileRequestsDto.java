package com.inssider.api.domains.profile;

import java.util.Optional;

public class UserProfileRequestsDto {
  public record PatchProfileMeRequest(
      Optional<String> nickname,
      Optional<String> profileUrl,
      Optional<String> bio,
      Optional<Boolean> accountVisible,
      Optional<Boolean> followerVisible) {}
}

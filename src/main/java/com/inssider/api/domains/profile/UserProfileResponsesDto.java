package com.inssider.api.domains.profile;

import java.time.LocalDateTime;

class UserProfileResponsesDto {

  record GetPrivateProfileResponse(String nickname, String profileUrl)
      implements GetProfileResponse {}

  record GetPublicProfileResponse(String nickname, String profileUrl, String bio)
      implements GetProfileResponse {}

  record GetProfileMeResponse(
      String nickname,
      String profileUrl,
      String bio,
      Boolean accountVisible,
      Boolean followerVisible)
      implements GetProfileResponse {}

  public sealed interface GetProfileResponse
      permits GetPrivateProfileResponse, GetPublicProfileResponse, GetProfileMeResponse {
    String nickname();

    String profileUrl();

    default String bio() {
      return null;
    }

    default Boolean accountVisible() {
      return null;
    }

    default Boolean followerVisible() {
      return null;
    }
  }

  public record PatchProfileMeResponse(GetProfileResponse data, LocalDateTime updatedAt) {}
}

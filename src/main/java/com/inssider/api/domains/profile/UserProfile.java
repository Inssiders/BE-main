package com.inssider.api.domains.profile;

import com.inssider.api.common.model.SoftDeleteable;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.profile.UserProfileDataTypes.ProfileContext;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetPrivateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileMeResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetPublicProfileResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserProfile extends SoftDeleteable {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private Long id;

  @OneToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  private String nickname;
  private String bio;
  private String profileUrl;

  @Builder.Default private boolean accountVisible = true;

  @Builder.Default private boolean followerVisible = true;

  /**
   * UserProfile 엔티티를 접근 수준에 따라 적절한 UserProfileDto로 변환합니다. 명시적으로 SELF 컨텍스트를 지정하지 않으면 엔티티의 기본 접근 수준에
   * 따라 변환됩니다.
   *
   * @return 변환된 UserProfileDto 객체
   */
  GetProfileResponse convertToDto(ProfileContext context) {
    return switch (context) {
      case SELF ->
          new GetProfileMeResponse(nickname, profileUrl, bio, accountVisible, followerVisible);
      default -> convertToDto();
    };
  }

  GetProfileResponse convertToDto() {
    var accessLevel = isAccountVisible() ? ProfileContext.PUBLIC : ProfileContext.PRIVATE;
    return switch (accessLevel) {
      case PRIVATE -> new GetPrivateProfileResponse(nickname, profileUrl);
      case PUBLIC -> new GetPublicProfileResponse(nickname, profileUrl, bio);
      case SELF ->
          new GetProfileMeResponse(nickname, profileUrl, bio, accountVisible, followerVisible);
    };
  }
}

package com.inssider.api.domains.profile;

import com.inssider.api.common.model.Auditable;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.profile.UserProfileDataTypes.ProfileContext;
import com.inssider.api.domains.profile.UserProfileResponsesDto.OwnerUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PrivateUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PublicUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UserProfileDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends Auditable {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private Long id;

  @OneToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;

  @NonNull private String nickname;

  private String bio;
  private String profileUrl;
  private boolean accountVisible;
  private boolean followerVisible;

  @Builder
  private UserProfile(
      @NonNull Account account,
      @NonNull String nickname,
      String bio,
      String profileUrl,
      boolean accountVisible,
      boolean followerVisible) {
    this.account = account;
    this.nickname = nickname;
    this.bio = bio;
    this.profileUrl = profileUrl;
    this.accountVisible = accountVisible;
    this.followerVisible = followerVisible;
  }

  /**
   * UserProfile 엔티티를 접근 수준에 따라 적절한 UserProfileDto로 변환합니다.
   *
   * @return 변환된 UserProfileDto 객체
   */
  UserProfileDto convertToDto() {
    var accessLevel = determineAccessLevel(this);
    return switch (accessLevel) {
      case PRIVATE -> new PrivateUserProfile(this.nickname, this.profileUrl);
      case PUBLIC -> new PublicUserProfile(this.nickname, this.profileUrl, this.bio);
      case SELF ->
          new OwnerUserProfile(
              this.nickname, this.profileUrl, this.bio, this.accountVisible, this.followerVisible);
    };
  }

  /**
   * 현재 UserProfile의 접근 수준을 결정합니다.
   *
   * @return ProfileContext 열거형 값 (PUBLIC, PRIVATE, SELF)
   */
  private ProfileContext determineAccessLevel(UserProfile profile) {
    // [ ] 인증 로직 추가 & PUBLIC, PRIVATE, SELF 구분 로직 구현
    return profile.isAccountVisible() ? ProfileContext.SELF : ProfileContext.PRIVATE;
  }
}

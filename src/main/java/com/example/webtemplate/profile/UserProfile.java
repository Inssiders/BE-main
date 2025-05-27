package com.example.webtemplate.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id
    @GeneratedValue
    private Long id;

    private String introduction;

    private String profileUrl;

    @NonNull
    private String nickname;

    private boolean accountVisibility;
    private boolean followerVisibility;

    @Builder
    private UserProfile(
            String introduction,
            String profileUrl,
            @NonNull String nickname,
            boolean accountVisibility,
            boolean followerVisibility) {
        this.introduction = introduction;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.accountVisibility = accountVisibility;
        this.followerVisibility = followerVisibility;
    }

}
package com.example.webtemplate.profile;

import com.example.webtemplate.account.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String introduction;

    private String profileUrl;

    @NonNull
    private String nickname;

    private boolean accountVisibility;
    private boolean followerVisibility;

    @Builder
    private UserProfile(
            @NonNull Account account,
            String introduction,
            String profileUrl,
            @NonNull String nickname,
            boolean accountVisibility,
            boolean followerVisibility) {
        this.account = account;
        this.introduction = introduction;
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.accountVisibility = accountVisibility;
        this.followerVisibility = followerVisibility;
    }

}
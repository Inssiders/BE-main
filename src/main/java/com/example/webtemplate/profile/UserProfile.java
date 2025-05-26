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

    @NonNull
    private String nickname;

    private String bio;
    private String profileUrl;
    private boolean accountVisible;
    private boolean followerVisibie;

    @Builder
    private UserProfile(
            @NonNull Account account,
            @NonNull String nickname,
            String bio,
            String profileUrl,
            boolean accountVisible,
            boolean followerVisibie) {
        this.account = account;
        this.nickname = nickname;
        this.bio = bio;
        this.profileUrl = profileUrl;
        this.accountVisible = accountVisible;
        this.followerVisibie = followerVisibie;
    }

}
package com.example.webtemplate.profile;

import java.time.LocalDateTime;

class UserProfileResponsesDto {

    record PrivateUserProfile(String nickname, String profileUrl) implements UserProfileDto {
    }

    record PublicUserProfile(String nickname, String profileUrl, String bio)
            implements UserProfileDto {
    }

    record OwnerUserProfile(String nickname, String profileUrl, String bio, Boolean accountVisible,
            Boolean followerVisible) implements UserProfileDto {
    }

    sealed interface UserProfileDto permits PrivateUserProfile, PublicUserProfile, OwnerUserProfile {
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

    record UpdateProfileResponse(UserProfileDto data, LocalDateTime updatedAt) {
    }

}
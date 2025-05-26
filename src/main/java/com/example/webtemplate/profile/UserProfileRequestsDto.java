package com.example.webtemplate.profile;

import java.util.Optional;

public class UserProfileRequestsDto {
    public record UpdateProfile(
        Long id,
        Optional<String> nickname,
        Optional<String> profileUrl,
        Optional<String> bio,
        Optional<Boolean> accountVisible,
        Optional<Boolean> followerVisible
    ) {}
}
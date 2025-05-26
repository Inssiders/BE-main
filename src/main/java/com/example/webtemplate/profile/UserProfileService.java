package com.example.webtemplate.profile;

import java.util.List;
import java.util.Optional;

import com.example.webtemplate.profile.UserProfileResponsesDto.UpdateProfileResponse;
import com.example.webtemplate.profile.UserProfileResponsesDto.UserProfileDto;

public interface UserProfileService {

    List<Long> getAllUserProfileIds();

    long count();

    UserProfileDto findUserProfileIdsById(Long id);

    UpdateProfileResponse updateUserProfile(
            Long id,
            Optional<String> nickname,
            Optional<String> profileUrl,
            Optional<String> bio,
            Optional<Boolean> accountVisible,
            Optional<Boolean> followerVisible);

}
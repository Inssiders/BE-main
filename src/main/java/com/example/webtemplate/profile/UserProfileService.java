package com.example.webtemplate.profile;

import java.util.List;

import com.example.webtemplate.profile.UserProfileResponsesDto.UserProfileDto;

public interface UserProfileService {

    List<Long> getAllUserProfileIds();

    long count();

    UserProfileDto findUserProfileIdsById(Long id);

}
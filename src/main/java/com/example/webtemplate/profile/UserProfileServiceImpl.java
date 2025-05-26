package com.example.webtemplate.profile;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.webtemplate.profile.UserProfileDataTypes.ProfileContext;
import com.example.webtemplate.profile.UserProfileResponsesDto.OwnerUserProfile;
import com.example.webtemplate.profile.UserProfileResponsesDto.PrivateUserProfile;
import com.example.webtemplate.profile.UserProfileResponsesDto.PublicUserProfile;
import com.example.webtemplate.profile.UserProfileResponsesDto.UserProfileDto;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository repository;

    UserProfileServiceImpl(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Long> getAllUserProfileIds() {
        return repository.findAll().stream()
                .map(profile -> profile.getId())
                .filter(id -> id != null)
                .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public UserProfileDto findUserProfileIdsById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found for id: " + id));

        // [ ] use ProfileContext.SELF only when user authenticated 
        var accessLevel = entity.isAccountVisible() ? ProfileContext.SELF : ProfileContext.PRIVATE;
        // accessLevel = ProfileContext.PUBLIC;

        return switch (accessLevel) {
            case ProfileContext.PRIVATE -> new PrivateUserProfile(entity.getNickname(), entity.getProfileUrl());
            case ProfileContext.PUBLIC ->
                new PublicUserProfile(entity.getNickname(), entity.getProfileUrl(), entity.getBio());
            case ProfileContext.SELF -> new OwnerUserProfile(entity.getNickname(), entity.getProfileUrl(),
                    entity.getBio(), entity.isAccountVisible(), entity.isFollowerVisibie());
        };
    }
}

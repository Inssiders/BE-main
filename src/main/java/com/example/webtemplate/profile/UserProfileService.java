package com.example.webtemplate.profile;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
class UserProfileService {

    private final UserProfileRepository repository;

    UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public List<Long> getAllUserProfileIds() {
        return repository.findAll().stream()
                .map(profile -> profile.getId())
                .filter(id -> id != null)
                .toList();
    }

}

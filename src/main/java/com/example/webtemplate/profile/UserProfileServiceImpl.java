package com.example.webtemplate.profile;

import java.util.List;

import org.springframework.stereotype.Service;

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

}

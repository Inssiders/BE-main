package com.example.webtemplate.profile;

import java.util.List;

public interface UserProfileService {

    List<Long> getAllUserProfileIds();

    long count();

}
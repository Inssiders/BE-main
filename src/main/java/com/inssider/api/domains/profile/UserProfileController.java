package com.inssider.api.domains.profile;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.common.response.StandardResponse.GetIndexResponse;
import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.profile.UserProfileRequestsDto.PatchProfileMeRequest;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetPrivateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileMeResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetPublicProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PatchProfileMeResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
class UserProfileController {

  private final UserProfileService service;

  @GetMapping
  public QueryResponse<GetProfileResponse> query(
      @ParameterObject
          @PageableDefault(
              size = 10,
              page = 0,
              sort = {"nickname", "createdAt"},
              direction = Sort.Direction.ASC)
          Pageable pageable,
      @RequestParam(required = false) String nickname) {
    return service.findUserProfilesByNickname(nickname, pageable);
  }

  @GetMapping("/{id}")
  ResponseEntity<ResponseWrapper<GetProfileResponse>> getProfile(@PathVariable("id") Long id) {
    var profileData = service.findUserProfileById(id);
    return switch (profileData) {
      case GetPublicProfileResponse pub -> BaseResponse.of(200, pub);
      case GetPrivateProfileResponse priv -> BaseResponse.of(200, priv);
      default -> BaseResponse.of(404, null);
    };
  }

  @GetMapping("/me")
  ResponseEntity<ResponseWrapper<GetProfileMeResponse>> getProfile(
      @AuthenticationPrincipal Account account) {
    var profileData = service.findUserProfileById(account.getId());
    return switch (profileData) {
      case GetProfileMeResponse owner -> BaseResponse.of(200, owner);
      default -> BaseResponse.of(403, null);
    };
  }

  @PatchMapping("/me")
  ResponseEntity<ResponseWrapper<PatchProfileMeResponse>> updateProfile(
      @AuthenticationPrincipal Account account, @RequestBody PatchProfileMeRequest profile) {
    var data =
        service.updateUserProfile(
            account.getId(),
            profile.nickname(),
            profile.profileUrl(),
            profile.bio(),
            profile.accountVisible(),
            profile.followerVisible());
    return BaseResponse.of(200, data);
  }

  @GetMapping("/index")
  ResponseEntity<ResponseWrapper<GetIndexResponse>> accountIndex() {
    var accountIds = service.getAllUserProfileIds();
    var data = new GetIndexResponse(accountIds);
    return BaseResponse.of(200, data);
  }
}

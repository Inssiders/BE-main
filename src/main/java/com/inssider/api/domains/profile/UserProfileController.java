package com.inssider.api.domains.profile;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.common.response.StandardResponse.IndexResponse;
import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.profile.UserProfileRequestsDto.UpdateProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.OwnerUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PrivateUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.PublicUserProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UpdateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UserProfileDto;
import lombok.RequiredArgsConstructor;
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
  public QueryResponse<UserProfileDto> query(
      @RequestParam("nickname") String nickname,
      @RequestParam("sort") String sort,
      @RequestParam("limit") int limit,
      @RequestParam("page") int page) {
    return service.findUserProfilesByNickname(nickname, sort, limit, page);
  }

  @GetMapping("/{id}")
  ResponseEntity<ResponseWrapper<UserProfileDto>> getProfile(@PathVariable("id") Long id) {
    var profileData = service.findUserProfileById(id);
    return switch (profileData) {
      case PublicUserProfile pub -> BaseResponse.of(200, pub);
      case PrivateUserProfile priv -> BaseResponse.of(200, priv);
      default -> BaseResponse.of(404, null);
    };
  }

  @GetMapping("/me")
  ResponseEntity<ResponseWrapper<OwnerUserProfile>> getProfile(
      @AuthenticationPrincipal Account account) {
    var profileData = service.findUserProfileById(account.getId());
    return switch (profileData) {
      case OwnerUserProfile owner -> BaseResponse.of(200, owner);
      default -> BaseResponse.of(403, null);
    };
  }

  @PatchMapping("/me")
  ResponseEntity<ResponseWrapper<UpdateProfileResponse>> updateProfile(
      @AuthenticationPrincipal Account account, @RequestBody UpdateProfile profile) {
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
  ResponseEntity<ResponseWrapper<IndexResponse>> accountIndex() {
    var accountIds = service.getAllUserProfileIds();
    var data = new IndexResponse(accountIds);
    return BaseResponse.of(200, data);
  }
}

package com.inssider.api.domains.profile;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.common.response.StandardResponse.IndexResponse;
import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.profile.UserProfileRequestsDto.UpdateProfile;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UpdateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.UserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
class UserProfileController {

  // 예약된 빈 `ProfileController`과의 충돌
  // -> `UserProfileController`로 이름 변경

  private final UserProfileService service;

  UserProfileController(UserProfileService service) {
    this.service = service;
  }

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
    var data = service.findUserProfileById(id);
    return BaseResponse.of(200, data);
  }

  @PatchMapping("/me")
  ResponseEntity<ResponseWrapper<UpdateProfileResponse>> updateProfile(
      @RequestBody UpdateProfile profile) {
    var data =
        service.updateUserProfile(
            profile.id(),
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

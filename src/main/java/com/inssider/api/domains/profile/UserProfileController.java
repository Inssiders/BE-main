package com.inssider.api.domains.profile;

import com.inssider.api.common.model.ServiceResult;
import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.common.response.BaseResponse.ResponseWrapper;
import com.inssider.api.common.response.StandardResponse.GetIndexResponse;
import com.inssider.api.common.response.StandardResponse.QueryResponse;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.profile.UserProfileDataTypes.ProfileContext;
import com.inssider.api.domains.profile.UserProfileRequestsDto.PatchProfileMeRequest;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetPrivateProfileResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileMeResponse;
import com.inssider.api.domains.profile.UserProfileResponsesDto.GetProfileResponse;
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

  // === Public API ===

  @GetMapping("/index")
  ResponseEntity<ResponseWrapper<GetIndexResponse>> accountIndexes() throws Throwable {
    var result = service.getAllUserProfileIds();
    return switch (result) {
      case ServiceResult.Success<GetIndexResponse, ?> success ->
          BaseResponse.of(200, success.value());
      case ServiceResult.Failure<GetIndexResponse, ? extends Throwable> failure ->
          throw failure.exception();
    };
  }

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
    return service.findUserProfilesByNickname(nickname, pageable).orElseThrow();
  }

  @GetMapping("/{id:\\d+}")
  ResponseEntity<ResponseWrapper<GetProfileResponse>> getProfile(@PathVariable("id") Long id) {

    var result = service.findUserProfileById(id, ProfileContext.PUBLIC).orElseThrow();

    return switch (result) {
      case GetPrivateProfileResponse priv -> BaseResponse.of(200, priv);
      default -> BaseResponse.of(200, result);
    };
  }

  // === Protected API ===

  @GetMapping("/me")
  ResponseEntity<ResponseWrapper<GetProfileMeResponse>> getProfile(
      @AuthenticationPrincipal Account account) {

    long accountId = account.getId();
    var result = service.findUserProfileById(accountId, ProfileContext.SELF).orElseThrow();

    return BaseResponse.of(200, (GetProfileMeResponse) result);
  }

  @PatchMapping("/me")
  ResponseEntity<ResponseWrapper<PatchProfileMeResponse>> updateProfile(
      @AuthenticationPrincipal Account account, @RequestBody PatchProfileMeRequest profile) {

    var result = service.updateUserProfile(account.getId(), profile).orElseThrow();

    return BaseResponse.of(200, result);
  }
}

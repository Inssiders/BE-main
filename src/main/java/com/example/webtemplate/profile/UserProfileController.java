package com.example.webtemplate.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webtemplate.common.response.BaseResponse;
import com.example.webtemplate.common.response.BaseResponse.ResponseWrapper;
import com.example.webtemplate.common.response.StandardResponse.IndexResponse;

@RestController
@RequestMapping("/profiles")
class UserProfileController {

  // 예약된 빈 `ProfileController`과의 충돌
  // -> `UserProfileController`로 이름 변경

  private final UserProfileService service;

  UserProfileController(UserProfileService service) {
    this.service = service;
  }

  @GetMapping("/index")
  ResponseEntity<ResponseWrapper<IndexResponse>> accountIndex() {
    var accountIds = service.getAllUserProfileIds();
    var data = new IndexResponse(accountIds);
    return BaseResponse.of(200, data);
  }
}

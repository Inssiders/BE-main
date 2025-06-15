package com.inssider.api.domains.like.controller;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.like.dto.LikeRequestDTO;
import com.inssider.api.domains.like.dto.LikeResponseDTO;
import com.inssider.api.domains.like.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  @PostMapping("{typeId}/like")
  ResponseEntity<BaseResponse.ResponseWrapper<LikeResponseDTO>> register(
      @AuthenticationPrincipal Account reqAccount,
      @PathVariable Long typeId,
      @Valid @RequestBody LikeRequestDTO reqBody) {
    LikeResponseDTO data = likeService.post(reqAccount, typeId, reqBody);
    return BaseResponse.of(200, data);
  }
}

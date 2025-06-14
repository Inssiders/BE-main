package com.inssider.api.domains.comment.controller;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.comment.dto.*;
import com.inssider.api.domains.comment.service.CommentService;
import jakarta.validation.Valid;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{memeId}")
  ResponseEntity<BaseResponse.ResponseWrapper<CommentCreateResponseDTO>> create(
      @AuthenticationPrincipal Account reqAccount,
      @PathVariable Long memeId,
      @Valid @RequestBody CommentCreateRequestDTO reqBody) {
    CommentCreateResponseDTO data = commentService.create(reqAccount, memeId, reqBody);
    return BaseResponse.of(201, data);
  }

  @PatchMapping("/delete/{commentId}")
  ResponseEntity<BaseResponse.ResponseWrapper<CommentDeleteResponseDTO>> delete(
          @AuthenticationPrincipal Account reqAccount, @PathVariable Long commentId) {
    CommentDeleteResponseDTO data = commentService.delete(reqAccount, commentId);
    return BaseResponse.of(200, data);
  }

  @PatchMapping("/{commentId}")
  ResponseEntity<BaseResponse.ResponseWrapper<CommentUpdateResponseDTO>> update(
          @AuthenticationPrincipal Account reqAccount, @PathVariable Long commentId, @RequestBody CommentUpdateRequestDTO reqBody){
    CommentUpdateResponseDTO data = commentService.update(reqAccount, commentId, reqBody);
    return BaseResponse.of(200, data);
  }

  @GetMapping("/{memeId}")
  ResponseEntity<BaseResponse.ResponseWrapper<List<CommentGetResponseDTO>>> get(
      @PathVariable Long memeId) {
    List<CommentGetResponseDTO> data = commentService.get(memeId);
    return BaseResponse.of(200, data);
  }
}

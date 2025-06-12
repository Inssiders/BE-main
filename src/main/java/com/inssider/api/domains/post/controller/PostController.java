package com.inssider.api.domains.post.controller;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.domains.post.dto.*;
import com.inssider.api.domains.post.service.PostService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @PostMapping
  ResponseEntity<BaseResponse.ResponseWrapper<PostResponseDTO>> register(
      @Valid @RequestBody PostCreateRequestDTO reqBody) {
    PostResponseDTO data = postService.create(reqBody);
    return BaseResponse.of(201, data);
  }

  @PatchMapping("/{memeId}")
  ResponseEntity<BaseResponse.ResponseWrapper<PostUpdateResponseDTO>> update(
      @PathVariable Long memeId, @RequestBody PostUpdateRequestDTO reqBody) {
    PostUpdateResponseDTO data = postService.update(memeId, reqBody);
    return BaseResponse.of(200, data);
  }

  @PatchMapping("/delete/{memeId}")
  ResponseEntity<BaseResponse.ResponseWrapper<PostDeleteResponseDTO>> delete(
      @PathVariable Long memeId) {
    PostDeleteResponseDTO data = postService.delete(memeId);
    return BaseResponse.of(200, data);
  }

  @GetMapping("/{memeId}")
  ResponseEntity<BaseResponse.ResponseWrapper<PostGetDetailResponseDTO>> getDetail(
      @PathVariable Long memeId) {
    PostGetDetailResponseDTO data = postService.getDetail(memeId);
    return BaseResponse.of(200, data);
  }

  @GetMapping("/sitemap")
  ResponseEntity<BaseResponse.ResponseWrapper<List<PostGetIdResponseDTO>>> getIds(
      @RequestParam(value = "since", required = false) LocalDate since) {
    List<PostGetIdResponseDTO> data = postService.getIds(since);
    return BaseResponse.of(200, data);
  }

  @GetMapping
  public ResponseEntity<BaseResponse.ResponseWrapper<PostCursorResponseDTO>> get(
      @ParameterObject PostCursorRequestDTO request) {
    PostCursorResponseDTO data = postService.get(request);
    return BaseResponse.of(200, data);
  }
}

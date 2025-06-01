package com.example.webtemplate.post.controller;

import com.example.webtemplate.common.response.BaseResponse;
import com.example.webtemplate.post.dto.*;
import com.example.webtemplate.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    ResponseEntity<BaseResponse.ResponseWrapper<PostCreateResponseDTO>> register(@Valid @RequestBody PostCreateRequestDTO reqBody) {
        PostCreateResponseDTO data = postService.create(reqBody);
        return BaseResponse.of(201, data);
    }

    @PatchMapping("/{memeId}")
    ResponseEntity<BaseResponse.ResponseWrapper<PostUpdateResponseDTO>> update(@PathVariable Long memeId, @RequestBody PostUpdateRequestDTO reqBody){
        PostUpdateResponseDTO data = postService.update(memeId, reqBody);
        return BaseResponse.of(200, data);
    }

    @PatchMapping("/delete/{memeId}")
    ResponseEntity<BaseResponse.ResponseWrapper<PostDeleteResponseDTO>> delete(@PathVariable Long memeId){
        PostDeleteResponseDTO data = postService.delete(memeId);
        return BaseResponse.of(200, data);
    }


}

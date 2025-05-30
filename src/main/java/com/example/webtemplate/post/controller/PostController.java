package com.example.webtemplate.post.controller;

import com.example.webtemplate.common.response.BaseResponse;
import com.example.webtemplate.post.dto.PostRequestDTO;
import com.example.webtemplate.post.dto.PostResponseDTO;
import com.example.webtemplate.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    ResponseEntity<BaseResponse.ResponseWrapper<PostResponseDTO>> register(@Valid @RequestBody PostRequestDTO reqBody) {
        PostResponseDTO data = postService.createPost(reqBody);
        return BaseResponse.of(201, data);
    }

}

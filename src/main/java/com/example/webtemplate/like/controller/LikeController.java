package com.example.webtemplate.like.controller;

import com.example.webtemplate.common.response.BaseResponse;
import com.example.webtemplate.like.dto.LikeRequestDTO;
import com.example.webtemplate.like.dto.LikeResponseDTO;
import com.example.webtemplate.like.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("{typeId}/like")
    ResponseEntity<BaseResponse.ResponseWrapper<LikeResponseDTO>> register(@PathVariable Long typeId, @Valid @RequestBody LikeRequestDTO reqBody) {
        LikeResponseDTO data = likeService.post(typeId, reqBody);
        return BaseResponse.of(200, data);
    }
}

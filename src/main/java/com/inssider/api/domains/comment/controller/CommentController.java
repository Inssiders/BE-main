package com.inssider.api.domains.comment.controller;

import com.inssider.api.common.response.BaseResponse;
import com.inssider.api.domains.comment.dto.CommentCreateRequestDTO;
import com.inssider.api.domains.comment.dto.CommentCreateResponseDTO;
import com.inssider.api.domains.comment.dto.CommentDeleteResponseDTO;
import com.inssider.api.domains.comment.dto.CommentGetResponseDTO;
import com.inssider.api.domains.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{memeId}")
    ResponseEntity<BaseResponse.ResponseWrapper<CommentCreateResponseDTO>> create(
            @PathVariable Long memeId, @Valid @RequestBody CommentCreateRequestDTO reqBody) {
        CommentCreateResponseDTO data = commentService.create(memeId, reqBody);
        return BaseResponse.of(201, data);
    }

    @PatchMapping("/delete/{commentId}")
    ResponseEntity<BaseResponse.ResponseWrapper<CommentDeleteResponseDTO>> delete(
            @PathVariable Long commentId) {
        CommentDeleteResponseDTO data = commentService.delete(commentId);
        return BaseResponse.of(200, data);
    }

    @GetMapping("/{memeId}")
    ResponseEntity<BaseResponse.ResponseWrapper<List<CommentGetResponseDTO>>> get(
            @PathVariable Long memeId) {
        List<CommentGetResponseDTO> data = commentService.get(memeId);
        return BaseResponse.of(200, data);
    }
}

package com.inssider.api.domains.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentCreateResponseDTO {

    private String content;
    private LocalDateTime createdAt;

}

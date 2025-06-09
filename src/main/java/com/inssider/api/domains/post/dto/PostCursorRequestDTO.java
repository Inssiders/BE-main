package com.inssider.api.domains.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCursorRequestDTO {
    private Long lastId;
    @Builder.Default
    private int size = 10;
    private String keyword;
    private Long categoryId;
    private String sort;
}
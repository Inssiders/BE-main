package com.inssider.api.domains.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostCursorResponseDTO {
    private List<PostDTO> content;
    private boolean hasNext;
    private Long nextCursor;
}

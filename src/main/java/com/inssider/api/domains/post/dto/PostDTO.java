package com.inssider.api.domains.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDTO {
    private Long accountId;
    private String title;
    private String nickname;
    private String profileUrl;
    private Long likeCount;
    private Long commentCount;
}

package com.example.webtemplate.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDeleteResponseDTO {
    private String title;
    private LocalDateTime deletedAt;
}

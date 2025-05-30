package com.example.webtemplate.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostResponseDTO {
    private String title;
    private String content;
    private String mediaUrl;
    private LocalDateTime mediaUploadTime;
    private String categoryName;
    private List<String> tags;
    private LocalDateTime createdAt;
}

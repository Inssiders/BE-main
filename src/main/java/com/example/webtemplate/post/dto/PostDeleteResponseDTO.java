package com.example.webtemplate.post.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDeleteResponseDTO {
    private String title;
    private LocalDateTime deletedAt;
}

package com.example.webtemplate.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class PostGetIdResponseDTO {
    private Long id;
    private LocalDateTime createdAt;

}

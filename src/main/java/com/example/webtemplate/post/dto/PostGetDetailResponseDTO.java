package com.example.webtemplate.post.dto;

import com.example.webtemplate.category.CategoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostGetDetailResponseDTO {
    private String writer; //엔티티 구조 수정 후 반영 예정
    private String title;
    private String content;
    private String mediaUrl;
    private LocalDateTime mediaUploadTime;
    private CategoryType categoryType;
    private List<String> tags;
    private LocalDateTime createdAt;
}

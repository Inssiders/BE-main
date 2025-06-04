package com.example.webtemplate.post.dto;

import com.example.webtemplate.category.CategoryType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostGetDetailResponseDTO {
  private String writer;
  private String title;
  private String content;
  private String mediaUrl;
  private LocalDateTime mediaUploadTime;
  private CategoryType categoryType;
  private List<String> tags;
  private LocalDateTime createdAt;
}

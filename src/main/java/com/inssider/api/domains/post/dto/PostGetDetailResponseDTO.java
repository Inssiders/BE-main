package com.inssider.api.domains.post.dto;

import com.inssider.api.domains.category.CategoryType;
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

package com.inssider.api.domains.post.dto;

import com.inssider.api.domains.category.CategoryType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateResponseDTO {

  private String title;
  private String content;
  private String mediaUrl;
  private LocalDateTime mediaUploadTime;
  private CategoryType categoryType;
  private List<String> tags;
  private LocalDateTime updatedAt;
}

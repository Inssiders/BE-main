package com.inssider.api.domains.post.dto;

import com.inssider.api.domains.category.CategoryType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class PostUpdateRequestDTO {

  private String title;
  private String content;
  private String mediaUrl;
  private LocalDateTime mediaUploadTime;
  private CategoryType categoryType;
  private List<String> tags;

  public boolean hasTitle() {
    return title != null;
  }

  public boolean hasContent() {
    return content != null;
  }

  public boolean hasMediaUrl() {
    return mediaUrl != null;
  }

  public boolean hasMediaUploadTime() {
    return mediaUploadTime != null;
  }

  public boolean hasCategoryType() {
    return categoryType != null;
  }

  public boolean hasTags() {
    return tags != null;
  }
}

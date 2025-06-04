package com.example.webtemplate.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDeleteResponseDTO {
  private String title;
  private LocalDateTime deletedAt;
}

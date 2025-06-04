package com.inssider.api.domains.post.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostGetIdResponseDTO {
  private Long id;
  private LocalDateTime createdAt;
}

package com.inssider.api.domains.comment.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateResponseDTO {

  private String content;
  private LocalDateTime createdAt;
}

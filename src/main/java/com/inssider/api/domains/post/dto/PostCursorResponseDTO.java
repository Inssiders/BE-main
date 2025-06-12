package com.inssider.api.domains.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCursorResponseDTO {
  private List<PostDTO> content;
  private boolean hasNext;
  private Long nextCursor;
}

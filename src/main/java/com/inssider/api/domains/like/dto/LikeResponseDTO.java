package com.inssider.api.domains.like.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDTO {
  private String targetType;
  private Long targetId;
  private Boolean liked;
  private Integer totalLikeCount;
}

package com.inssider.api.domains.like.mapper;

import com.inssider.api.domains.like.dto.LikeResponseDTO;
import com.inssider.api.domains.like.entity.Like;

public class LikeMapper {

  public static LikeResponseDTO toUnLikedDTO(
      String targetType, Long targetId, boolean liked, int totalLikeCount) {
    return LikeResponseDTO.builder()
        .targetType(targetType)
        .targetId(targetId)
        .liked(liked)
        .totalLikeCount(totalLikeCount)
        .build();
  }

  public static LikeResponseDTO toLikedDTO(Like like, boolean liked, int totalLikeCount) {
    return LikeResponseDTO.builder()
        .targetType(like.getTargetType().name())
        .targetId(like.getTargetId())
        .liked(liked)
        .totalLikeCount(totalLikeCount)
        .build();
  }
}

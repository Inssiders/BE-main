package com.inssider.api.domains.comment.mapper;

import com.inssider.api.domains.comment.dto.CommentCreateResponseDTO;
import com.inssider.api.domains.comment.dto.CommentDeleteResponseDTO;
import com.inssider.api.domains.comment.dto.CommentGetResponseDTO;
import com.inssider.api.domains.comment.dto.CommentUpdateResponseDTO;
import com.inssider.api.domains.comment.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommentMapper {

  public static CommentCreateResponseDTO toDTO(Comment comment) {
    return CommentCreateResponseDTO.builder()
        .content(comment.getContent())
        .createdAt(comment.getCreatedAt())
        .build();
  }

  public static CommentDeleteResponseDTO deleteDTO(Comment comment) {
    return CommentDeleteResponseDTO.builder()
        .content(comment.getContent())
        .deletedAt(comment.getDeletedAt())
        .build();
  }

  public static CommentGetResponseDTO toResponseDTO(Comment comment) {
    return CommentGetResponseDTO.builder()
        .id(comment.getId())
        .content(comment.getContent())
        .writer(comment.getAccount().getProfile().getNickname())
        .createdAt(comment.getCreatedAt())
        .children(new ArrayList<>())
        .build();
  }

  public static List<CommentGetResponseDTO> toGetResponseDTO(List<Comment> comments) {
    Map<Long, CommentGetResponseDTO> commentMap =
        comments.stream().collect(Collectors.toMap(Comment::getId, CommentMapper::toResponseDTO));

    return comments.stream()
        .peek(
            comment -> {
              if (comment.getParentComment() != null) {
                CommentGetResponseDTO currentDto = commentMap.get(comment.getId());
                CommentGetResponseDTO parentDto =
                    commentMap.get(comment.getParentComment().getId());
                parentDto.addChild(currentDto);
              }
            })
        .filter(comment -> comment.getParentComment() == null)
        .map(comment -> commentMap.get(comment.getId()))
        .collect(Collectors.toList());
  }

  public static CommentUpdateResponseDTO toUpdateDTO(Comment comment) {
    return CommentUpdateResponseDTO.builder()
        .content(comment.getContent())
        .updatedAt(comment.getUpdatedAt())
        .build();
  }
}

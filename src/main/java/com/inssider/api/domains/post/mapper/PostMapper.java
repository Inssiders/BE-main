package com.inssider.api.domains.post.mapper;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.category.entity.Category;
import com.inssider.api.domains.post.dto.PostCreateRequestDTO;
import com.inssider.api.domains.post.dto.PostDeleteResponseDTO;
import com.inssider.api.domains.post.dto.PostGetDetailResponseDTO;
import com.inssider.api.domains.post.dto.PostResponseDTO;
import com.inssider.api.domains.post.dto.PostUpdateResponseDTO;
import com.inssider.api.domains.post.entity.Post;
import java.util.List;

public class PostMapper {

  public static Post dtoToPost(Account account, Category category, PostCreateRequestDTO reqBody) {
    return Post.builder()
        .title(reqBody.getTitle())
        .content(reqBody.getContent())
        .mediaUrl(reqBody.getMediaUrl())
        .mediaUploadTime(reqBody.getMediaUploadTime())
        .account(account)
        .category(category)
        .build();
  }

  public static PostResponseDTO postToDTO(Post post) {
    List<String> tags = post.getPostTags().stream().map(t -> t.getTag().getName()).toList();

    return PostResponseDTO.builder()
        .title(post.getTitle())
        .content(post.getContent())
        .mediaUrl(post.getMediaUrl())
        .mediaUploadTime(post.getMediaUploadTime())
        .categoryType(post.getCategory().getType())
        .tags(tags)
        .createdAt(post.getCreatedAt())
        .build();
  }

  public static PostUpdateResponseDTO postToUpdateDTO(Post post) {
    List<String> tags = post.getPostTags().stream().map(t -> t.getTag().getName()).toList();
    return PostUpdateResponseDTO.builder()
        .title(post.getTitle())
        .content(post.getContent())
        .mediaUrl(post.getMediaUrl())
        .mediaUploadTime(post.getMediaUploadTime())
        .categoryType(post.getCategory().getType())
        .tags(tags)
        .updatedAt(post.getUpdatedAt())
        .build();
  }

  public static PostDeleteResponseDTO deleteDTO(Post post) {
    return PostDeleteResponseDTO.builder()
        .title(post.getTitle())
        .deletedAt(post.getDeletedAt())
        .build();
  }

  // 엔티티 구조 변경 후 수정 예정
  public static PostGetDetailResponseDTO toGetDetailDTO(Post post) {
    List<String> tags = post.getPostTags().stream().map(t -> t.getTag().getName()).toList();

    return PostGetDetailResponseDTO.builder()
        .title(post.getTitle())
        .content(post.getContent())
        .mediaUrl(post.getMediaUrl())
        .mediaUploadTime(post.getMediaUploadTime())
        .categoryType(post.getCategory().getType())
        .tags(tags)
        .createdAt(post.getCreatedAt())
        .build();
  }
}

package com.example.webtemplate.post.mapper;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.post.dto.PostDeleteResponseDTO;
import com.example.webtemplate.post.dto.PostCreateRequestDTO;
import com.example.webtemplate.post.dto.PostCreateResponseDTO;
import com.example.webtemplate.post.dto.PostUpdateResponseDTO;
import com.example.webtemplate.post.entity.Post;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

    public static PostCreateResponseDTO postToDTO(Post post) {
        List<String> tags = post.getPostTags().stream()
                .map(t -> t.getTag().getName())
                .toList();

        return PostCreateResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaUploadTime(post.getMediaUploadTime())
                .categoryType(post.getCategory().getType())
                .tags(tags)
                .createdAt(post.getCreated_at())
                .build();
    }

    public static PostUpdateResponseDTO postToUpdateDTO(Post post) {
        List<String> tags = post.getPostTags().stream()
                .map(t -> t.getTag().getName())
                .toList();
        return PostUpdateResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaUploadTime(post.getMediaUploadTime())
                .categoryType(post.getCategory().getType())
                .tags(tags)
                .updatedAt(post.getUpdated_at())
                .build();
    }

    public static PostDeleteResponseDTO deleteDTO(Post post) {
        return PostDeleteResponseDTO.builder()
                .title(post.getTitle())
                .deletedAt(post.getDeleted_at())
                .build();
    }

}

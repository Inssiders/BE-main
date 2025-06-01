package com.example.webtemplate.post.mapper;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.category.CategoryType;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.post.dto.PostRequestDTO;
import com.example.webtemplate.post.dto.PostResponseDTO;
import com.example.webtemplate.post.dto.PostUpdateResponseDTO;
import com.example.webtemplate.post.entity.Post;

import com.example.webtemplate.tag.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    public static Post dtoToPost(Account account, Category category, PostRequestDTO reqBody) {
        return Post.builder()
                .title(reqBody.getTitle())
                .content(reqBody.getContent())
                .mediaUrl(reqBody.getMediaUrl())
                .mediaUploadTime(reqBody.getMediaUploadTime())
                .account(account)
                .category(category)
                .build();
    }

    public static PostResponseDTO postToDTO(CategoryType categoryType, List<Tag> tag, Post post) {
        List<String> tags = tag.stream()
                .map(t -> t.getName())
                .toList();

        return PostResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaUploadTime(post.getMediaUploadTime())
                .categoryType(categoryType)
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


}

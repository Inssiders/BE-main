package com.example.webtemplate.post.mapper;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.post.dto.PostRequestDTO;
import com.example.webtemplate.post.dto.PostResponseDTO;
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

    public static PostResponseDTO postToDTO(String categoryName, List<Tag> tag, Post post) {
        List<String> tags = tag.stream()
                .map(t -> t.getName())
                .toList();

        return PostResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .mediaUrl(post.getMediaUrl())
                .mediaUploadTime(post.getMediaUploadTime())
                .categoryName(categoryName)
                .tags(tags)
                .createdAt(post.getCreated_at())
                .build();
    }


}

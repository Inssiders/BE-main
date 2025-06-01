package com.example.webtemplate.post.service;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.account.AccountRepository;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.category.service.CategoryService;
import com.example.webtemplate.post.dto.PostRequestDTO;
import com.example.webtemplate.post.dto.PostResponseDTO;
import com.example.webtemplate.post.dto.PostUpdateRequestDTO;
import com.example.webtemplate.post.dto.PostUpdateResponseDTO;
import com.example.webtemplate.post.entity.Post;
import com.example.webtemplate.post.mapper.PostMapper;
import com.example.webtemplate.post.repository.PostRepository;
import com.example.webtemplate.tag.service.TagService;
import com.example.webtemplate.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    //인증 적용 후 삭제 예정
    private final AccountRepository accountRepository;

    public PostResponseDTO create(PostRequestDTO reqBody) {
        //인증 적용 후 삭제 예정
        Account account = accountRepository.findById(1L).get();
        Category category = categoryService.getCategory(reqBody.getCategoryType())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 오류 발생"));

        List<Tag> tags = tagService.findOrCreateTags(reqBody.getTags());

        Post post = PostMapper.dtoToPost(account, category, reqBody);
        post.addTags(tags);

        postRepository.save(post);

        return PostMapper.postToDTO(category.getType(), tags, post);
    }

    public PostUpdateResponseDTO update(Long memeId, PostUpdateRequestDTO reqBody) {
        Post post = postRepository.findById(memeId).orElseThrow(() -> new IllegalArgumentException("게시글 오류 발생"));

        if(reqBody.hasTitle()) {
            post.updateTitle(reqBody.getTitle());
        }

        if(reqBody.hasContent()) {
            post.updateContent(reqBody.getContent());
        }

        if(reqBody.hasMediaUrl()) {
            post.updateMediaUrl(reqBody.getMediaUrl());
        }

        if(reqBody.hasMediaUploadTime()) {
            post.updateMediaUploadTime(reqBody.getMediaUploadTime());
        }

        postRepository.save(post);

        return PostMapper.postToUpdateDTO(post);
    }
}

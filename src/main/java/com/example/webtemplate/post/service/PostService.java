package com.example.webtemplate.post.service;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.account.AccountRepository;
import com.example.webtemplate.category.entity.Category;
import com.example.webtemplate.category.service.CategoryService;
import com.example.webtemplate.post.dto.PostRequestDTO;
import com.example.webtemplate.post.dto.PostResponseDTO;
import com.example.webtemplate.post.entity.Post;
import com.example.webtemplate.post.mapper.PostMapper;
import com.example.webtemplate.post.repository.PostRepository;
import com.example.webtemplate.tag.service.TagService;
import com.example.webtemplate.tag.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    //인증 적용 후 삭제 예정
    private final AccountRepository accountRepository;

    public PostResponseDTO createPost(PostRequestDTO reqBody) {
        Account account = accountRepository.findById(1L).get(); //인증 적용 후 삭제 예정
        Category category = categoryService.getCategory(reqBody.getCategoryType())
                .orElseThrow(() -> new IllegalArgumentException("카테고리 오류 발생"));

        List<Tag> tags = tagService.findOrCreateTags(reqBody.getTags());

        Post post = PostMapper.dtoToPost(account, category, reqBody);
        post.addTags(tags);

        postRepository.save(post);

        return PostMapper.postToDTO(category.getType(), tags, post);
    }
}

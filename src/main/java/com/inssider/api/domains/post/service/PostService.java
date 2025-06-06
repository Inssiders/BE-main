package com.inssider.api.domains.post.service;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.category.entity.Category;
import com.inssider.api.domains.category.service.CategoryService;
import com.inssider.api.domains.post.dto.PostCreateRequestDTO;
import com.inssider.api.domains.post.dto.PostDeleteResponseDTO;
import com.inssider.api.domains.post.dto.PostGetDetailResponseDTO;
import com.inssider.api.domains.post.dto.PostGetIdResponseDTO;
import com.inssider.api.domains.post.dto.PostResponseDTO;
import com.inssider.api.domains.post.dto.PostUpdateRequestDTO;
import com.inssider.api.domains.post.dto.PostUpdateResponseDTO;
import com.inssider.api.domains.post.entity.Post;
import com.inssider.api.domains.post.mapper.PostMapper;
import com.inssider.api.domains.post.repository.PostRepository;
import com.inssider.api.domains.tag.entity.Tag;
import com.inssider.api.domains.tag.service.TagService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

  private final PostRepository postRepository;
  private final CategoryService categoryService;
  private final TagService tagService;

  // 인증 적용 후 삭제 예정
  private final AccountService accountService;

  public PostResponseDTO create(PostCreateRequestDTO reqBody) {
    // 인증 적용 후 삭제 예정
    Account account = accountService.findById(1L).get();
    Category category =
        categoryService
            .getCategory(reqBody.getCategoryType())
            .orElseThrow(() -> new IllegalArgumentException("카테고리 오류 발생"));

    List<Tag> tags = tagService.findOrCreateTags(reqBody.getTags());

    Post post = PostMapper.dtoToPost(account, category, reqBody);
    post.addTags(tags);

    postRepository.save(post);

    return PostMapper.postToDTO(post);
  }

  public PostUpdateResponseDTO update(Long memeId, PostUpdateRequestDTO reqBody) {
    Post currentPost =
        postRepository
            .findByIdWithTag(memeId)
            .orElseThrow(() -> new IllegalArgumentException("게시글 오류 발생"));

    if (reqBody.hasTitle()) {
      currentPost.updateTitle(reqBody.getTitle());
    }

    if (reqBody.hasContent()) {
      currentPost.updateContent(reqBody.getContent());
    }

    if (reqBody.hasMediaUrl()) {
      currentPost.updateMediaUrl(reqBody.getMediaUrl());
    }

    if (reqBody.hasMediaUploadTime()) {
      currentPost.updateMediaUploadTime(reqBody.getMediaUploadTime());
    }

    if (reqBody.hasCategoryType()) {
      Category category =
          categoryService
              .getCategory(reqBody.getCategoryType())
              .orElseThrow(() -> new IllegalArgumentException("카테고리 오류 발생"));
      currentPost.updateCategory(category);
    }

    if (reqBody.hasTags()) {
      List<Tag> tags = tagService.findOrCreateTags(reqBody.getTags());
      currentPost.getPostTags().clear();
      currentPost.addTags(tags);
    }

    Post updatedPost = postRepository.save(currentPost);
    return PostMapper.postToUpdateDTO(updatedPost);
  }

  public PostDeleteResponseDTO delete(Long memeId) {
    Post currentPost =
        postRepository
            .findByIdWithTag(memeId)
            .orElseThrow(() -> new IllegalArgumentException("게시글 오류 발생"));
    currentPost.updateIsDeleted();
    currentPost.updateDeletedAt();
    Post updatedPost = postRepository.save(currentPost);

    return PostMapper.deleteDTO(updatedPost);
  }

  public PostGetDetailResponseDTO getDetail(Long memeId) {
    Post currentPost =
        postRepository
            .findByIdWithTag(memeId)
            .orElseThrow(() -> new IllegalArgumentException("게시글 오류 발생"));
    return PostMapper.toGetDetailDTO(currentPost);
  }

  public List<PostGetIdResponseDTO> getIds(LocalDate localDate) {
    if (localDate == null) {
      return postRepository.findAllIds();
    }
    LocalDateTime since = localDate.atStartOfDay();
    return postRepository.findPostsByCreatedAtAfter(since);
  }

  public boolean isPost(Long postId) {
    return postRepository.existsById(postId);
  }
}

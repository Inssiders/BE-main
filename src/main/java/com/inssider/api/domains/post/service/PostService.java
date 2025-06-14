package com.inssider.api.domains.post.service;

import com.inssider.api.common.service.VerifyService;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.category.entity.Category;
import com.inssider.api.domains.category.service.CategoryService;
import com.inssider.api.domains.post.dto.*;
import com.inssider.api.domains.post.entity.Post;
import com.inssider.api.domains.post.mapper.PostMapper;
import com.inssider.api.domains.post.repository.PostRepository;
import com.inssider.api.domains.tag.entity.Tag;
import com.inssider.api.domains.tag.service.TagService;
import jakarta.transaction.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService implements VerifyService {

  private final PostRepository postRepository;
  private final CategoryService categoryService;
  private final TagService tagService;

  private final AccountService accountService;

  public PostResponseDTO create(Account reqAccount, PostCreateRequestDTO reqBody) {
    Account account =
        accountService.findById(reqAccount.getId()).orElseThrow(NoSuchElementException::new);
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

  public PostUpdateResponseDTO update(Account reqAccount, Long memeId, PostUpdateRequestDTO reqBody){

    Post currentPost =
        postRepository
            .findByIdWithTag(memeId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 콘텐츠입니다."));

    if(!validateId(currentPost.getAccount().getId(), reqAccount.getId())){
      throw new AccessDeniedException("수정 권한이 없습니다.");
    }

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

  public PostDeleteResponseDTO delete(Account reqAccount, Long memeId) {
    Post currentPost =
        postRepository
            .findByIdWithTag(memeId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 콘텐츠입니다."));

    if(!validateId(currentPost.getAccount().getId(), reqAccount.getId())){
      throw new AccessDeniedException("삭제 권한이 없습니다.");
    }

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

  public Post get(Long postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 콘텐츠입니다."));
  }

  public PostCursorResponseDTO get(PostCursorRequestDTO request) {
    // 인증 적용 후 삭제 예정
    Account account = accountService.findById(1L).get();

    if ("post".equals(request.getProfile_filter())) {
      return getPostsByAccount(request, account.getId());
    } else if ("like".equals(request.getProfile_filter())) {
      return getLikedPostsByAccount(request, account.getId());
    } else {
      return getAllPosts(request);
    }
  }

  public PostCursorResponseDTO getAllPosts(PostCursorRequestDTO request) {
    return postRepository.findPostsWithCursor(request, null);
  }

  public PostCursorResponseDTO getPostsByAccount(PostCursorRequestDTO request, Long accountId) {
    return postRepository.findPostsByAccount(request, accountId);
  }

  public PostCursorResponseDTO getLikedPostsByAccount(
      PostCursorRequestDTO request, Long accountId) {
    return postRepository.findLikedPostsByAccount(request, accountId);
  }
}

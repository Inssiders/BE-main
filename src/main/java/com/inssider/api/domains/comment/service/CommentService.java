package com.inssider.api.domains.comment.service;

import com.inssider.api.common.service.VerifyService;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.comment.dto.*;
import com.inssider.api.domains.comment.entity.Comment;
import com.inssider.api.domains.comment.mapper.CommentMapper;
import com.inssider.api.domains.comment.repository.CommentRepository;
import com.inssider.api.domains.post.entity.Post;
import com.inssider.api.domains.post.service.PostService;
import jakarta.transaction.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService implements VerifyService {

  private final CommentRepository commentRepository;
  private final PostService postService;
  // 인증 적용 후 삭제 예정
  private final AccountService accountService;

  public CommentCreateResponseDTO create(
      Account reqAccount, Long memeId, CommentCreateRequestDTO requestDTO) {

    Account account =
        accountService.findById(reqAccount.getId()).orElseThrow(NoSuchElementException::new);
    Post post = postService.get(memeId);

    Comment parentComment = null;
    if (requestDTO.getParentCommentId() != null) {
      parentComment = findById(requestDTO.getParentCommentId());
    }

    Comment comment =
        Comment.builder()
            .content(requestDTO.getContent())
            .post(post)
            .account(account)
            .parentComment(parentComment)
            .build();

    if (parentComment != null) {
      parentComment.addChild(comment);
    }

    Comment createdComment = commentRepository.save(comment);
    return CommentMapper.toDTO(createdComment);
  }

  public CommentDeleteResponseDTO delete(Long commentId) {
    Comment currentComment = findById(commentId);

    if (!currentComment.getChildComments().isEmpty())
      throw new IllegalStateException("하위 댓글이 존재하여 삭제가 불가능합니다.");

    currentComment.updateIsDeleted();
    currentComment.updateDeletedAt();
    Comment updatedComment = commentRepository.save(currentComment);

    return CommentMapper.deleteDTO(updatedComment);
  }

  public List<CommentGetResponseDTO> get(Long memeId) {
    if (postService.isPost(memeId)) {
      List<Comment> comments = commentRepository.findAllByPostIdWithAccount(memeId);
      return CommentMapper.toGetResponseDTO(comments);
    }
    throw new NoSuchElementException("존재하지 않는 콘텐츠입니다.");
  }

  public CommentUpdateResponseDTO update(Account reqAccount, Long commentId, CommentUpdateRequestDTO reqBody) throws AccessDeniedException {
    Comment currentComment = findById(commentId);

    if(!validateId(currentComment.getAccount().getId(), reqAccount.getId())){
      throw new AccessDeniedException("수정 권한이 없습니다.");
    }

    currentComment.updateContent(reqBody.getContent());
    commentRepository.flush();
    return CommentMapper.toUpdateDTO(currentComment);
  }

  public Comment findById(Long commentId) {
    return commentRepository
        .findById(commentId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 댓글입니다."));
  }
}

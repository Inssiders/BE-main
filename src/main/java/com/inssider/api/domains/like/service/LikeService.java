package com.inssider.api.domains.like.service;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.comment.service.CommentService;
import com.inssider.api.domains.like.LikeTargetType;
import com.inssider.api.domains.like.dto.LikeRequestDTO;
import com.inssider.api.domains.like.dto.LikeResponseDTO;
import com.inssider.api.domains.like.entity.Like;
import com.inssider.api.domains.like.mapper.LikeMapper;
import com.inssider.api.domains.like.repository.LikeRepository;
import com.inssider.api.domains.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final AccountService accountService;

  public LikeResponseDTO post(Account reqAccount, Long targetId, LikeRequestDTO requestDTO) {
    boolean liked = true;
    Account account = accountService.findById(reqAccount.getId())
            .orElseThrow(() -> new IllegalStateException("인증 절차 완료 후 접근해주세요."));
    validateTarget(targetId, requestDTO.getTargetType());
    boolean currentLiked =
        likeRepository.existsByAccountIdAndTargetTypeAndTargetId(
            account.getId(), requestDTO.getTargetType(), targetId);
    int totalLikeCount =
        likeRepository.countByTargetTypeAndTargetId(requestDTO.getTargetType(), targetId);

    if (currentLiked) {
      likeRepository.deleteByAccountIdAndTargetTypeAndTargetId(
              account.getId(), requestDTO.getTargetType(), targetId);
      liked = false;
      return LikeMapper.toUnLikedDTO(
          requestDTO.getTargetType().name(), targetId, liked, totalLikeCount - 1);
    }

    Like like =
        Like.builder()
            .account(account)
            .targetType(requestDTO.getTargetType())
            .targetId(targetId)
            .build();

    likeRepository.save(like);
    return LikeMapper.toLikedDTO(like, liked, totalLikeCount + 1);
  }

  private void validateTarget(Long targetId, LikeTargetType targetType) {
    switch (targetType) {
      case POST:
        if (!postService.isPost(targetId)) {
          throw new EntityNotFoundException("존재하지 않는 콘텐츠입니다.");
        }
        break;
      case COMMENT:
        if (!commentService.isComment(targetId)) {
          throw new EntityNotFoundException("존재하지 않는 댓글입니다.");
        }
        break;
    }
  }
}

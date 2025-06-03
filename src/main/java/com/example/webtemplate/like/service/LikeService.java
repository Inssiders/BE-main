package com.example.webtemplate.like.service;

import com.example.webtemplate.account.Account;
import com.example.webtemplate.account.AccountRepository;
import com.example.webtemplate.like.LikeTargetType;
import com.example.webtemplate.like.dto.LikeRequestDTO;
import com.example.webtemplate.like.dto.LikeResponseDTO;
import com.example.webtemplate.like.entity.Like;
import com.example.webtemplate.like.mapper.LikeMapper;
import com.example.webtemplate.like.repository.LikeRepository;
import com.example.webtemplate.post.service.PostService;
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
    //인증 적용 후 삭제 예정
    private final AccountRepository accountRepository;

    public LikeResponseDTO post(Long targetId, LikeRequestDTO requestDTO) {
        boolean liked = true;
        //인증 적용 후 삭제 예정
        Account account = accountRepository.findById(1L).get();

        validateTarget(targetId, requestDTO.getTargetType());
        boolean currentLiked = likeRepository.existsByAccountIdAndTargetTypeAndTargetId(account.getId(), requestDTO.getTargetType(), targetId);
        int totalLikeCount = likeRepository.countByTargetTypeAndTargetId(requestDTO.getTargetType(), targetId);

        if (currentLiked) {
            likeRepository.deleteByAccountIdAndTargetTypeAndTargetId(account.getId(), requestDTO.getTargetType(), targetId);
            liked = false;
            return LikeMapper.toUnLikedDTO(requestDTO.getTargetType().name(), targetId, liked, totalLikeCount-1);
        }

        Like like = Like.builder()
                .account(account)
                .targetType(requestDTO.getTargetType())
                .targetId(targetId)
                .build();

        likeRepository.save(like);
        return LikeMapper.toLikedDTO(like, liked, totalLikeCount+1);
    }

    private void validateTarget(Long targetId, LikeTargetType targetType) {
        switch (targetType) {
            case POST:
                if (!postService.isPost(targetId)) {
                    throw new EntityNotFoundException("존재하지 않는 게시글입니다.");
                }
                break;
            case COMMENT: //추가 예정
                break;
        }
    }
}
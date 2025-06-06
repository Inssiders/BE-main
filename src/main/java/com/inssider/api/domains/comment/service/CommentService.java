package com.inssider.api.domains.comment.service;

import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.AccountService;
import com.inssider.api.domains.category.service.CategoryService;
import com.inssider.api.domains.comment.dto.CommentCreateRequestDTO;
import com.inssider.api.domains.comment.dto.CommentCreateResponseDTO;
import com.inssider.api.domains.comment.entity.Comment;
import com.inssider.api.domains.comment.mapper.CommentMapper;
import com.inssider.api.domains.comment.repository.CommentRepository;
import com.inssider.api.domains.post.entity.Post;
import com.inssider.api.domains.post.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    // 인증 적용 후 삭제 예정
    private final AccountService accountService;

    public CommentCreateResponseDTO create(Long memeId, CommentCreateRequestDTO requestDTO) {
        // 인증 적용 후 삭제 예정
        Account account = accountService.findById(2L).get();
        Post post = postService.get(memeId);

        Comment parentComment = null;
        if (requestDTO.getParentCommentId() != null) {
            parentComment = getParent(requestDTO.getParentCommentId());
        }

        Comment comment = Comment.builder()
                .content(requestDTO.getContent())
                .post(post)
                .account(account)
                .parentComment(parentComment)
                .build();

        Comment createdComment = commentRepository.save(comment);
        return CommentMapper.toDTO(createdComment);
    }

    public Comment getParent(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상위 댓글입니다."));
    }
}

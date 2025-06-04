package com.example.webtemplate.like.dto;

import com.example.webtemplate.like.LikeTargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeRequestDTO {

    @NotNull(message = "좋아요 대상 타입을 입력해주세요.")
    private LikeTargetType targetType;

}

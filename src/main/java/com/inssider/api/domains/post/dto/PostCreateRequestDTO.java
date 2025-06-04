package com.inssider.api.domains.post.dto;

import com.inssider.api.domains.category.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateRequestDTO {

  @NotBlank(message = "제목을 입력해주세요.")
  @Size(max = 255, message = "제목을 255자 이내로 작성해주세요.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  @NotBlank(message = "밈 url을 입력해주세요.")
  private String mediaUrl;

  private LocalDateTime mediaUploadTime;

  private CategoryType categoryType;

  private List<String> tags;
}

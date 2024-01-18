package com.ssafy.A509.diary.dto;

import com.ssafy.A509.account.model.User;
import com.ssafy.A509.diary.model.Category;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryResponse {
  @NotBlank
  private Long diaryId;

  @NotBlank
  private String content;

  @NotBlank
  private Category category;

  @NotBlank
  private int emoji;

  @NotBlank
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

}

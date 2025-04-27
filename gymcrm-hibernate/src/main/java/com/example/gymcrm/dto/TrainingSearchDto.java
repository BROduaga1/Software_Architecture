package com.example.gymcrm.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class TrainingSearchDto {
    @Nullable
    private String username;
    @Nullable
    private LocalDateTime fromDate;
    @Nullable
    private LocalDateTime toDate;
    @Nullable
    private Boolean isTrainee;

}

package com.example.gymcrm.dto;

import com.example.gymcrm.domain.TrainingType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TrainingDto {
    private Long id;

    @NotNull(message = "Trainer must not be null")
    @Valid
    private TrainerDto trainer;

    @NotNull(message = "Trainee must not be null")
    @Valid
    private TraineeDto trainee;

    @NotNull(message = "Name must not be null")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @NotNull(message = "Training type must not be null")
    private TrainingType type;

    @NotNull(message = "Date must not be null")
    private LocalDateTime date;

    @NotNull(message = "Duration must not be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Long durationMinutes;
}

package com.example.gymcrm.dto;

import com.example.gymcrm.domain.TrainingType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@Data
@ToString
public class TrainerDto {
    private Long id;
    private TrainingType specialization;
    @Valid
    private UserDto user;

}

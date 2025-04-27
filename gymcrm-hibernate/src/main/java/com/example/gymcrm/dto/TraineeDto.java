package com.example.gymcrm.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@Data
@ToString
public class TraineeDto {
    private Long id;

    @Nullable
    @ToString.Exclude
    private LocalDate dateOfBirth;

    @Nullable
    @ToString.Exclude
    private String address;

    @Valid
    private UserDto user;

}

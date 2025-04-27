package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.TrainerDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TrainerMapperTest {
    private final TrainerMapper mapper = new TrainerMapperImpl();

    @Test
    void shouldMapTrainerToTrainerDto() {
        Trainer trainer = TestData.createTrainer();
        TrainerDto trainerDto = mapper.toDto(trainer);
        assertThat(trainer.getId()).isEqualTo(trainerDto.getId());
        assertThat(trainer.getUser().getUsername()).isEqualTo(trainerDto.getUser().getUsername());
    }

    @Test
    void shouldMapTrainerDtoToTrainer() {
        TrainerDto trainerDto = TestData.createTrainerDto();
        Trainer trainer = mapper.toEntity(trainerDto);
        assertThat(trainerDto.getId()).isEqualTo(trainer.getId());
        assertThat(trainerDto.getUser().getUsername()).isEqualTo(trainer.getUser().getUsername());
    }

    @Test
    void shouldUpdateTrainerFromTrainerDto() {
        TrainerDto trainerDto = TestData.createUpdatedTrainerDto();
        Trainer trainer = TestData.createTrainer();
        mapper.updateEntityFromDto(trainerDto, trainer);
        assertThat(trainerDto.getUser().getUsername()).isEqualTo(trainer.getUser().getUsername());
    }

    @Test
    void shouldHandleNullTrainerToTrainerDto() {
        TrainerDto trainerDto = mapper.toDto(null);
        assertThat(trainerDto).isNull();
    }

    @Test
    void shouldHandleNullTrainerDtoToTrainer() {
        Trainer trainer = mapper.toEntity(null);
        assertThat(trainer).isNull();
    }

    @Test
    void shouldHandleNullUpdateTrainerFromTrainerDto() {
        Trainer trainer = TestData.createTrainer();
        mapper.updateEntityFromDto(null, trainer);
        assertThat(trainer).isNotNull();
    }

    @Test
    void shouldMapTrainerListToTrainerDtoList() {
        Trainer trainer = TestData.createTrainer();
        assertThat(mapper.toDtoList(List.of(trainer)).get(0).getId()).isEqualTo(trainer.getId());
    }

    @Test
    void shouldMapEmptyTrainerListToEmptyTrainerDtoList() {
        List<TrainerDto> trainerDtos = mapper.toDtoList(List.of());
        Assertions.assertThat(trainerDtos).isEmpty();
    }

    @Test
    void shouldHandleNullUserDtoToUser() {
        Trainer trainer = TestData.createTrainer();
        trainer.setUser(null);
        TrainerDto trainerDto = mapper.toDto(trainer);
        Assertions.assertThat(trainerDto).isNotNull();
        Assertions.assertThat(trainerDto.getUser()).isNull();
    }

    @Test
    void shouldHandleNullUserToUserDto() {
        TrainerDto trainerDto = TestData.createTrainerDto();
        trainerDto.setUser(null);
        Trainer trainer = mapper.toEntity(trainerDto);
        Assertions.assertThat(trainer).isNotNull();
        Assertions.assertThat(trainer.getUser()).isNull();
    }

}

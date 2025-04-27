package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.TraineeDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TraineeMapperTest {

    private final TraineeMapperImpl mapper = new TraineeMapperImpl();

    @Test
    void shouldMapTraineeToTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        TraineeDto traineeDto = mapper.toDto(trainee);
        Assertions.assertThat(trainee.getId()).isEqualTo(traineeDto.getId());
        Assertions.assertThat(trainee.getUser()).isEqualTo(TestData.createTrainee().getUser());
    }

    @Test
    void shouldMapTraineeDtoToTrainee() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        Trainee trainee = mapper.toEntity(traineeDto);
        Assertions.assertThat(traineeDto.getId()).isEqualTo(trainee.getId());
        Assertions.assertThat(traineeDto.getUser()).isEqualTo(TestData.createTraineeDto().getUser());
    }

    @Test
    void shouldUpdateTraineeFromTraineeDto() {
        TraineeDto traineeDto = TestData.createUpdatedTraineeDto();
        Trainee trainee = TestData.createTrainee();
        mapper.updateEntityFromDto(traineeDto, trainee);
        Assertions.assertThat(traineeDto.getUser().getUsername()).isEqualTo(TestData.createTraineeDto().getUser().getUsername());
    }

    @Test
    void shouldHandleNullTraineeToTraineeDto() {
        TraineeDto traineeDto = mapper.toDto(null);
        Assertions.assertThat(traineeDto).isNull();
    }

    @Test
    void shouldHandleNullTraineeDtoToTrainee() {
        Trainee trainee = mapper.toEntity(null);
        Assertions.assertThat(trainee).isNull();
    }

    @Test
    void shouldHandleNullUserDtoToUser() {
        Trainee trainee = TestData.createTrainee();
        trainee.setUser(null);
        TraineeDto traineeDto = mapper.toDto(trainee);
        Assertions.assertThat(traineeDto).isNotNull();
        Assertions.assertThat(traineeDto.getUser()).isNull();
    }

    @Test
    void shouldHandleNullUserToUserDto() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        traineeDto.setUser(null);
        Trainee trainee = mapper.toEntity(traineeDto);
        Assertions.assertThat(trainee).isNotNull();
        Assertions.assertThat(trainee.getUser()).isNull();
    }

    @Test
    void shouldHandleNullUpdateTraineeFromTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        mapper.updateEntityFromDto(null, trainee);
        Assertions.assertThat(trainee).isNotNull();
        Assertions.assertThat(trainee.getUser().getUsername()).isNotNull();
    }
}

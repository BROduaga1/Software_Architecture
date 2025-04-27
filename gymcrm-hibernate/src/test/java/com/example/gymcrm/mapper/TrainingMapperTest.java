package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingMapperTest {

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TrainingMapperImpl mapper;

    @Test
    void shouldMapTrainingToTrainingDto() {
        when(trainerMapper.toDto(any())).thenReturn(TestData.createTrainerDto());
        when(traineeMapper.toDto(any())).thenReturn(TestData.createTraineeDto());
        Training training = TestData.createTraining();
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(training.getId()).isEqualTo(trainingDto.getId());
        assertThat(training.getName()).isEqualTo(trainingDto.getName());
        assertThat(training.getTrainee().getId()).isEqualTo(trainingDto.getTrainee().getId());
        assertThat(training.getTrainer().getId()).isEqualTo(trainingDto.getTrainer().getId());
    }

    @Test
    void shouldMapTrainingDtoToTraining() {
        when(trainerMapper.toEntity(any())).thenReturn(TestData.createTrainer());
        when(traineeMapper.toEntity(any())).thenReturn(TestData.createTrainee());
        TrainingDto trainingDto = TestData.createTrainingDto();
        Training training = mapper.toEntity(trainingDto);
        assertThat(trainingDto.getId()).isEqualTo(training.getId());
        assertThat(trainingDto.getName()).isEqualTo(training.getName());
        assertThat(trainingDto.getTrainee().getId()).isEqualTo(training.getTrainee().getId());
        assertThat(trainingDto.getTrainer().getId()).isEqualTo(training.getTrainer().getId());
    }

    @Test
    void shouldHandleNullTrainingToTrainingDto() {
        TrainingDto trainingDto = mapper.toDto(null);
        assertThat(trainingDto).isNull();
    }

    @Test
    void shouldHandleNullTrainingDtoToTraining() {
        Training training = mapper.toEntity(null);
        assertThat(training).isNull();
    }

    @Test
    void shouldMapTrainingListToTrainingDtoList() {
        when(trainerMapper.toDto(any())).thenReturn(TestData.createTrainerDto());
        when(traineeMapper.toDto(any())).thenReturn(TestData.createTraineeDto());
        Training training = TestData.createTraining();
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(mapper.toDtoList(List.of(training))).containsExactlyInAnyOrder(trainingDto);
    }

    @Test
    void shouldHandleNullTrainingListToTrainingDtoList() {
        List<TrainingDto> trainingDtos = mapper.toDtoList(null);
        assertThat(trainingDtos).isNull();
    }

    @Test
    void shouldMapEmptyTrainingListToTrainingDtoList() {
        List<TrainingDto> trainingDtos = mapper.toDtoList(List.of());
        assertThat(trainingDtos).isEmpty();
    }
}

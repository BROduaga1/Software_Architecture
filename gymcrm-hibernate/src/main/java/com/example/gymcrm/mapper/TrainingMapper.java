package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class, TraineeMapper.class})
public interface TrainingMapper {

    TrainingDto toDto(Training entity);

    Training toEntity(TrainingDto dto);

    List<TrainingDto> toDtoList(List<Training> trainings);
}

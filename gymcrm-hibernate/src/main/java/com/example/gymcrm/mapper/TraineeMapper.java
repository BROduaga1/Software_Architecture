package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.TraineeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public
interface TraineeMapper {

    TraineeDto toDto(Trainee entity);

    Trainee toEntity(TraineeDto dto);

    void updateEntityFromDto(TraineeDto traineeDto, @MappingTarget Trainee trainee);
}


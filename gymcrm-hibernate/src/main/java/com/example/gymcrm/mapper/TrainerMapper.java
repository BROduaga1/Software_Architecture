package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.TrainerDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDto toDto(Trainer entity);

    Trainer toEntity(TrainerDto dto);

    void updateEntityFromDto(TrainerDto trainerDto, @MappingTarget Trainer trainer);

    List<TrainerDto> toDtoList(List<Trainer> trainers);
}

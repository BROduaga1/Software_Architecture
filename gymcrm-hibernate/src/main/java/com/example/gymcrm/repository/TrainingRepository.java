package com.example.gymcrm.repository;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingSearchDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository {
    Training save(Training training);

    Optional<Training> findById(long id);

    List<Training> findAllByCriteria(TrainingSearchDto searchDto);

}

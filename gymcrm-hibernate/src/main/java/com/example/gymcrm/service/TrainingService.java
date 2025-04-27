package com.example.gymcrm.service;

import com.example.gymcrm.dto.TrainingDto;
import com.example.gymcrm.dto.TrainingSearchDto;

import java.util.List;

public interface TrainingService {

    /**
     * Retrieves a training by its ID.
     *
     * @param id the ID of the training
     * @return the TrainingDto
     */
    TrainingDto getById(Long id);

    /**
     * Creates a new training.
     *
     * @param trainingDto the TrainingDto containing the training details
     * @return the created TrainingDto
     */
    TrainingDto create(TrainingDto trainingDto);

    /**
     * Finds all trainings that match the given criteria.
     *
     * @param trainingSearchDto the criteria for searching trainings
     * @return the list of TrainingDto that match the criteria
     */
    List<TrainingDto> findAllByCriteria(TrainingSearchDto trainingSearchDto);
}

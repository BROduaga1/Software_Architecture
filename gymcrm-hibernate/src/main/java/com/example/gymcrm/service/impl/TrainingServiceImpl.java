package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingDto;
import com.example.gymcrm.dto.TrainingSearchDto;
import com.example.gymcrm.mapper.TrainingMapper;
import com.example.gymcrm.repository.TrainingRepository;
import com.example.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    private static final String TRAINING_NOT_FOUND = "Training wasn't found with id:";

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public TrainingDto getById(Long id) {
        LOGGER.info("Selecting training with id {}", id);
        return trainingRepository.findById(id)
                .map(trainingMapper::toDto)
                .orElseThrow(() -> {
                    LOGGER.info(TRAINING_NOT_FOUND + id);
                    return new NoSuchElementException(TRAINING_NOT_FOUND + id);
                });
    }

    @Override
    public TrainingDto create(TrainingDto trainingDto) {
        Training training = trainingMapper.toEntity(trainingDto);
        training.setId(null);
        trainingRepository.save(training);
        LOGGER.info("Saved training with ID: {}", training.getId());
        return trainingMapper.toDto(training);
    }

    @Override
    public List<TrainingDto> findAllByCriteria(TrainingSearchDto trainingSearchDto) { // TODO: change to Pageable
        LOGGER.info("Selecting trainings by criteria: {}", trainingSearchDto);
        return trainingMapper.toDtoList(trainingRepository.findAllByCriteria(trainingSearchDto));
    }

}

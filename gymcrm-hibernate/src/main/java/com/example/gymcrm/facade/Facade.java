package com.example.gymcrm.facade;

import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.dto.TrainingDto;
import com.example.gymcrm.dto.TrainingSearchDto;
import com.example.gymcrm.security.Authenticated;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
@RequiredArgsConstructor
public class Facade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TraineeDto getTraineeByUsername(String username) {
        return traineeService.getByUsername(username);
    }

    public TrainingDto getTrainingById(Long id) {
        return trainingService.getById(id);
    }

    public TraineeDto createTrainee(@Valid TraineeDto traineeDto) {
        return traineeService.create(traineeDto);
    }

    public TrainerDto createTrainer(@Valid TrainerDto trainerDto) {
        return trainerService.create(trainerDto);
    }

    public TrainingDto createTraining(@Valid TrainingDto trainingDto) {
        return trainingService.create(trainingDto);
    }

    @Authenticated
    public void updateTrainee(@Valid TraineeDto traineeDto) {
        traineeService.update(traineeDto);
    }

    @Authenticated
    public void updateTrainer(@Valid TrainerDto trainerDto) {
        trainerService.update(trainerDto);
    }

    @Authenticated
    public TrainerDto getTrainerByUsername(String username) {
        return trainerService.getByUsername(username);
    }

    @Authenticated
    public boolean changeTrainerStatus(String username) {
        return trainerService.changeStatus(username);
    }

    @Authenticated
    public boolean isTrainerAuthenticated(String username, String password) {
        return trainerService.isAuthenticated(username, password);
    }

    @Authenticated
    public void changeTrainerPassword(String username, String lastPassword, String newPassword) {
        trainerService.changePassword(username, lastPassword, newPassword);
    }

    @Authenticated
    public void deleteTraineeByUsername(String username) {
        traineeService.deleteByUsername(username);
    }

    @Authenticated
    public boolean isTraineeAuthenticated(String username, String password) {
        return traineeService.isAuthenticated(username, password);
    }

    @Authenticated
    public void changeTraineePassword(String username, String lastPassword, String newPassword) {
        traineeService.changePassword(username, lastPassword, newPassword);
    }

    @Authenticated
    public boolean changeTraineeStatus(String username) {
        return traineeService.changeStatus(username);
    }

    @Authenticated
    public List<TrainingDto> findAllTrainingsByCriteria(@Valid TrainingSearchDto trainingSearchDto) {
        return trainingService.findAllByCriteria(trainingSearchDto);
    }

    @Authenticated
    public List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername) {
        return trainerService.getUnassignedTrainersListByUsername(traineeUsername);
    }

    @Authenticated
    public List<TrainerDto> updateTrainersListByUsername(String traineeUsername, @Valid List<String> trainers) {
        return traineeService.updateTrainersListByUsername(traineeUsername, trainers);
    }
}

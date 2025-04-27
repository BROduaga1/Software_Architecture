package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.User;
import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.mapper.TraineeMapper;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.UsernameGeneratorService;
import com.example.gymcrm.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private static final String TRAINEE_NOT_FOUND = "Trainee wasn't found with username:";

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final UsernameGeneratorService usernameGeneratorService;
    private final PasswordGenerator passwordGenerator;
    private final TrainerMapper trainerMapper;
    private final TrainerService trainerService;

    private void generatePasswordAndUsername(User user) {
        user.setPassword(passwordGenerator.generatePassword());
        user.setUsername(usernameGeneratorService.generateUsername(user.getFirstName(), user.getLastName()));
    }

    @Override
    public TraineeDto create(TraineeDto traineeDto) {
        Trainee trainee = traineeMapper.toEntity(traineeDto);
        generatePasswordAndUsername(trainee.getUser());
        trainee.setId(null);
        trainee.getUser().setId(null);
        traineeRepository.save(trainee);
        LOGGER.info("Saved trainee with ID: {}", trainee.getId());
        return traineeMapper.toDto(trainee);
    }

    @Override
    public TraineeDto getByUsername(String username) {
        return traineeMapper.toDto(getEntityByUsername(username));
    }


    public Trainee getEntityByUsername(String username) {
        LOGGER.info("Selecting trainee with id {}", username);
        return traineeRepository.findByUsername(username).orElseThrow(() -> {
            LOGGER.info(TRAINEE_NOT_FOUND + username);
            return new NoSuchElementException(TRAINEE_NOT_FOUND + username);
        });
    }

    @Override
    public TraineeDto update(TraineeDto traineeDto) {
        Trainee trainee = getEntityByUsername(traineeDto.getUser().getUsername());

        traineeMapper.updateEntityFromDto(traineeDto, trainee);
        Trainee updatedTrainee = traineeRepository.save(trainee);
        LOGGER.info("Trainee with ID {} updated", traineeDto.getUser().getId());
        return traineeMapper.toDto(updatedTrainee);
    }

    @Override
    public void deleteByUsername(String username) {
        getEntityByUsername(username);
        traineeRepository.deleteByUsername(username);
        LOGGER.info("Removed trainee with Id: {}", username);
    }

    @Override
    public boolean isAuthenticated(String username, String password) {
        Trainee existingTrainee = traineeRepository.findByUsername(username).orElseThrow(() -> {
            LOGGER.error(TRAINEE_NOT_FOUND + username);
            return new NoSuchElementException(TRAINEE_NOT_FOUND + username);
        });

        return password.equals(existingTrainee.getUser().getPassword());
    }

    @Override
    public void changePassword(String username, String lastPassword, String newPassword) {
        if (isAuthenticated(username, lastPassword)) {
            Trainee existingTrainee = getEntityByUsername(username);
            existingTrainee.getUser().setPassword(newPassword);
            traineeRepository.save(existingTrainee);
        } else {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    @Override
    public boolean changeStatus(String username) {
        Trainee existingTrainee = getEntityByUsername(username);
        existingTrainee.getUser().setIsActive(!existingTrainee.getUser().getIsActive());
        traineeRepository.save(existingTrainee);
        return traineeRepository.save(existingTrainee).getUser().getIsActive();
    }

    @Override
    public List<TrainerDto> updateTrainersListByUsername(String traineeUsername, List<String> trainerUsernames) {
        Trainee existingTrainee = getEntityByUsername(traineeUsername);
        List<Trainer> trainerList = trainerUsernames.stream().map(trainerService::getEntityByUsername).toList();
        existingTrainee.setTrainers(trainerList);
        traineeRepository.save(existingTrainee);
        return trainerMapper.toDtoList(trainerList);
    }
}

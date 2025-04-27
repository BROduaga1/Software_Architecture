package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.User;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TrainerRepository;
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
public class TrainerServiceImpl implements TrainerService {
    private static final String TRAINER_NOT_FOUND = "Trainer wasn't found with username:";

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UsernameGeneratorService usernameGeneratorService;
    private final PasswordGenerator passwordGenerator;

    private void generatePasswordAndUsername(User user) {
        String username = usernameGeneratorService.generateUsername(user.getFirstName(), user.getLastName());
        user.setUsername(username);
        String password = passwordGenerator.generatePassword();
        user.setPassword(password);
    }

    @Override
    public TrainerDto create(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        trainer.setId(null);
        trainer.getUser().setId(null);
        generatePasswordAndUsername(trainer.getUser());
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toDto(savedTrainer);
    }

    @Override
    public TrainerDto getByUsername(String username) {
        return trainerMapper.toDto(getEntityByUsername(username));
    }

    public Trainer getEntityByUsername(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(TRAINER_NOT_FOUND + username));
    }

    @Override
    public TrainerDto update(TrainerDto trainerDto) {
        Trainer trainer = getEntityByUsername(trainerDto.getUser().getUsername());

        trainerMapper.updateEntityFromDto(trainerDto, trainer);
        Trainer updatedTrainer = trainerRepository.save(trainer);
        LOGGER.info("Trainer with ID {} updated", trainerDto.getUser().getId());
        return trainerMapper.toDto(updatedTrainer);
    }

    @Override
    public boolean isAuthenticated(String username, String password) {
        Trainer trainer = getEntityByUsername(username);
        return trainer.getUser().getPassword().equals(password);
    }

    @Override
    public void changePassword(String username, String lastPassword, String newPassword) {
        Trainer trainer = getEntityByUsername(username);
        if (trainer.getUser().getPassword().equals(lastPassword)) {
            trainer.getUser().setPassword(newPassword);
            trainerRepository.save(trainer);
        } else {
            throw new IllegalArgumentException("Incorrect last password");
        }
    }

    @Override
    public boolean changeStatus(String username) {
        Trainer existingTrainer = getEntityByUsername(username);
        existingTrainer.getUser().setIsActive(!existingTrainer.getUser().getIsActive());
        trainerRepository.save(existingTrainer);
        return trainerRepository.save(existingTrainer).getUser().getIsActive();
    }

    @Override
    public List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername) {
        return trainerRepository.getUnassignedTrainersList(traineeUsername).stream()
                .map(trainerMapper::toDto)
                .toList();
    }
}

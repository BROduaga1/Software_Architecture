package com.example.gymcrm.service;

import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.TrainerDto;

import java.util.List;

public interface TrainerService {

    /**
     * Retrieves a TrainerDto by username.
     *
     * @param username the username of the trainer
     * @return the TrainerDto
     */
    TrainerDto getByUsername(String username);

    /**
     * Retrieves a Trainer entity by username.
     *
     * @param username the username of the trainer
     * @return the Trainer entity
     */
    Trainer getEntityByUsername(String username);

    /**
     * Creates a new Trainer.
     *
     * @param trainerDto the TrainerDto containing the trainer details
     * @return the created TrainerDto
     */
    TrainerDto create(TrainerDto trainerDto);

    /**
     * Updates an existing Trainer.
     *
     * @param trainerDto the TrainerDto containing the updated trainer details
     * @return the updated TrainerDto
     */
    TrainerDto update(TrainerDto trainerDto);

    /**
     * Changes the status of a Trainer.
     *
     * @param username the username of the trainer
     * @return the new status of the trainer
     */
    boolean changeStatus(String username);

    /**
     * Checks if a Trainer is authenticated.
     *
     * @param username the username of the trainer
     * @param password the password of the trainer
     * @return true if the trainer is authenticated, false otherwise
     */
    boolean isAuthenticated(String username, String password);

    /**
     * Changes the password of a Trainer.
     *
     * @param username the username of the trainer
     * @param lastPassword the current password of the trainer
     * @param newPassword the new password for the trainer
     */
    void changePassword(String username, String lastPassword, String newPassword);

    /**
     * Retrieves a list of unassigned trainers by trainee username.
     *
     * @param traineeUsername the username of the trainee
     * @return the list of unassigned TrainerDto
     */
    List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername);
}

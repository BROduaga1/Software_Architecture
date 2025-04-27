package com.example.gymcrm.service;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;

import java.util.List;
import java.util.NoSuchElementException;


public interface TraineeService {

    /**
     * Retrieves a trainee by their username.
     *
     * @param username the username of the trainee
     * @return the trainee DTO
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    TraineeDto getByUsername(String username) throws NoSuchElementException;


    /**
     * Retrieves a trainee by their username.
     *
     * @param username the username of the trainee
     * @return the trainee entity
     */
    Trainee getEntityByUsername(String username);


    /**
     * Creates a new trainee.
     *
     * @param traineeDto the trainee DTO
     * @return the created trainee DTO
     * @throws IllegalArgumentException if the trainee data is invalid
     */
    TraineeDto create(TraineeDto traineeDto) throws IllegalArgumentException;

    /**
     * Updates an existing trainee.
     *
     * @param traineeDto the trainee DTO
     * @return the updated trainee DTO
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    TraineeDto update(TraineeDto traineeDto) throws NoSuchElementException;

    /**
     * Deletes a trainee by their username.
     *
     * @param username the username of the trainee
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    void deleteByUsername(String username) throws NoSuchElementException;

    /**
     * Checks if a trainee is authenticated.
     *
     * @param username the username of the trainee
     * @param password the password of the trainee
     * @return true if the trainee is authenticated, false otherwise
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    boolean isAuthenticated(String username, String password) throws NoSuchElementException;

    /**
     * Changes the password of a trainee.
     *
     * @param username     the username of the trainee
     * @param lastPassword the current password of the trainee
     * @param newPassword  the new password of the trainee
     * @throws NoSuchElementException   if no trainee is found with the given username
     * @throws IllegalArgumentException if the current password is incorrect
     */
    void changePassword(String username, String lastPassword, String newPassword) throws NoSuchElementException, IllegalArgumentException;

    /**
     * Changes the status of a trainee.
     *
     * @param username the username of the trainee
     * @return the new status of the trainee
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    boolean changeStatus(String username) throws NoSuchElementException;


    /**
     * Retrieves a list of unassigned trainers for a trainee.
     *
     * @param traineeUsername the username of the trainee
     * @return the list of unassigned trainers
     * @throws NoSuchElementException if no trainee is found with the given username
     */
    List<TrainerDto> updateTrainersListByUsername(String traineeUsername, List<String> trainerUsernames);
}

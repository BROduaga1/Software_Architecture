package com.example.gymcrm.repository;

import com.example.gymcrm.domain.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Trainer save(Trainer trainer);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findAll();

    List<Trainer> getUnassignedTrainersList(String traineeUsername);
}

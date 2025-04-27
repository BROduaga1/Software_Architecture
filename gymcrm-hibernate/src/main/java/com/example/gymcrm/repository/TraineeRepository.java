package com.example.gymcrm.repository;

import com.example.gymcrm.domain.Trainee;

import java.util.Optional;

public interface TraineeRepository {
    Trainee save(Trainee trainee);

    Optional<Trainee> findByUsername(String username);

    void deleteByUsername(String username);

}

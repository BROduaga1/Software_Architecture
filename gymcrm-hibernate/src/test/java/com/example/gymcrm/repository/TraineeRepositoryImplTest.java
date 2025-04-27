package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.config.AppConfig;
import com.example.gymcrm.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TraineeRepositoryImplTest {

    @Autowired
    private TraineeRepository traineeRepository;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = TestData.createTrainee();
    }

    @Test
    void shouldReturnPresentTraineeByUsername() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        Optional<Trainee> result = traineeRepository.findByUsername(savedTrainee.getUser().getUsername());
        assertTrue(result.isPresent());
        assertEquals(savedTrainee, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTraineeNotFoundByUsername() {
        Optional<Trainee> result = traineeRepository.findByUsername("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        traineeRepository.deleteByUsername(savedTrainee.getUser().getUsername());
        Optional<Trainee> result = traineeRepository.findByUsername(savedTrainee.getUser().getUsername());
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTraineeWithGeneratedIdWhenIdIsNull() {
        trainee.setId(null);
        trainee.getUser().setId(null);
        traineeRepository.save(trainee);
        assertNotNull(trainee.getId());
    }

    @Test
    void shouldSaveTraineeWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = 5L;
        trainee.setId(existingId);
        traineeRepository.save(trainee);
        assertEquals(existingId, trainee.getId());
    }

    @Test
    void shouldChangeStatus() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        String username = savedTrainee.getUser().getUsername();
        boolean initialStatus = savedTrainee.getUser().getIsActive();
        savedTrainee.getUser().setIsActive(!initialStatus);
        traineeRepository.save(savedTrainee);
        boolean newStatus = traineeRepository.findByUsername(username).orElseThrow().getUser().getIsActive();
        assertNotEquals(initialStatus, newStatus);

        savedTrainee.getUser().setIsActive(!newStatus);
        traineeRepository.save(savedTrainee);
        boolean finalStatus = traineeRepository.findByUsername(username).orElseThrow().getUser().getIsActive();
        assertNotEquals(newStatus, finalStatus);
    }

}

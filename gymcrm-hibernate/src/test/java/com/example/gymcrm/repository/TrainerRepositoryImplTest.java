package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.config.AppConfig;
import com.example.gymcrm.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TrainerRepositoryImplTest {

    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = TestData.createTrainer();
    }

    @Test
    void shouldReturnPresentTrainerByUsername() {
        Trainer savedTrainer = trainerRepository.save(trainer);
        Optional<Trainer> result = trainerRepository.findByUsername(savedTrainer.getUser().getUsername());
        assertTrue(result.isPresent());
        assertEquals(savedTrainer, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTrainerNotFoundByUsername() {
        Optional<Trainer> result = trainerRepository.findByUsername("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTrainerWithGeneratedIdWhenIdIsNull() {
        trainer.setId(null);
        trainer.getUser().setId(null);
        trainerRepository.save(trainer);
        assertNotNull(trainer.getId());
    }

    @Test
    void shouldSaveTrainerWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = 5L;
        trainer.setId(existingId);
        trainerRepository.save(trainer);
        assertEquals(existingId, trainer.getId());
    }

    @Test
    void shouldFindAllTrainers() {
        Trainer savedTrainer1 = trainerRepository.save(trainer);
        Trainer savedTrainer2 = TestData.createTrainer();
        savedTrainer2.setId(null);
        savedTrainer2.getUser().setId(null);
        savedTrainer2.getUser().setUsername("another");
        trainerRepository.save(savedTrainer2);
        List<Trainer> trainers = trainerRepository.findAll();
        assertTrue(trainers.contains(savedTrainer1));
        assertTrue(trainers.contains(savedTrainer2));
    }
}

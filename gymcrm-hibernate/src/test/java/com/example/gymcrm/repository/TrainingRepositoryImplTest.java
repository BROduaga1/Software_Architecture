package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.config.AppConfig;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.gymcrm.TestData.createSearchDto;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TrainingRepositoryImplTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    private Training training;

    @BeforeEach
    void setUp() {
        Trainer trainer = trainerRepository.save(TestData.createTrainer());
        Trainee trainee = traineeRepository.save(TestData.createTrainee());
        training = TestData.createTraining();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
    }

    @Test
    void shouldReturnPresentTrainingById() {
        Training savedTraining = trainingRepository.save(training);
        Optional<Training> result = trainingRepository.findById(savedTraining.getId());
        assertTrue(result.isPresent());
        assertEquals(savedTraining, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTrainingNotFoundById() {
        Optional<Training> result = trainingRepository.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTrainingWithGeneratedIdWhenIdIsNull() {
        training.setId(null);
        trainingRepository.save(training);
        assertNotNull(training.getId());
    }

    @Test
    void shouldSaveTrainingWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = 5L;
        training.setId(existingId);
        trainingRepository.save(training);
        assertEquals(existingId, training.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true, true",
            "true, false, false, false",
            "false, true, true, true",
            "false, false, false, false"
    })
    void shouldFindByCriteria(boolean isTrainee, boolean expectUsername, boolean expectFromDate, boolean expectToDate) {
        Training savedTraining = trainingRepository.save(training);
        TrainingSearchDto searchDto = createSearchDto(savedTraining, isTrainee, expectUsername, expectFromDate, expectToDate);

        List<Training> trainings = trainingRepository.findAllByCriteria(searchDto);
        boolean expectedMatch = expectUsername && expectFromDate && expectToDate;

        assertEquals(expectedMatch, trainings.contains(savedTraining));
    }

}

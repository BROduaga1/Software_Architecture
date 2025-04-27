package com.example.gymcrm.facade;

import com.example.gymcrm.TestData;
import com.example.gymcrm.config.AppConfig;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.dto.TrainingDto;
import com.example.gymcrm.dto.TrainingSearchDto;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.TrainingTypeRepository;
import com.example.gymcrm.security.AuthenticationContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.gymcrm.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class FacadeIT {

    @Autowired
    private Facade facade;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    private TrainingDto trainingDtoWithUsers;

    @BeforeEach
    void setUp() {
        TrainerDto trainerDto = facade.createTrainer(createTrainerDto());
        TraineeDto traineeDto = facade.createTrainee(createTraineeDto());
        trainingDtoWithUsers = createTrainingDto();
        trainingDtoWithUsers.setTrainee(traineeDto);
        trainingDtoWithUsers.setTrainer(trainerDto);

        Trainee traineeForAuth = traineeRepository.findByUsername(traineeDto.getUser().getUsername()).orElseThrow();
        AuthenticationContext.setPassword(traineeForAuth.getUser().getPassword());
        AuthenticationContext.setUsername(traineeForAuth.getUser().getUsername());
    }

    @Test
    void shouldCreateAndGetTrainee() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        TraineeDto result = facade.getTraineeByUsername(createdTrainee.getUser().getUsername());
        assertEquals(createdTrainee, result);
    }

    @Test
    void shouldUpdateTrainee() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        createdTrainee.getUser().setFirstName("Updated");
        createdTrainee.setAddress("Updated");
        facade.updateTrainee(createdTrainee);
        TraineeDto result = facade.getTraineeByUsername(createdTrainee.getUser().getUsername());
        assertEquals("Updated", result.getUser().getFirstName());
        assertEquals("Updated", result.getAddress());
    }

    @Test
    void shouldDeleteTrainee() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        facade.deleteTraineeByUsername(createdTrainee.getUser().getUsername());
        String username = createdTrainee.getUser().getUsername();
        assertThrows(NoSuchElementException.class, () -> facade.getTraineeByUsername(username), TRAINEE_NOT_FOUND + username);
    }

    @Test
    void shouldAuthenticateTrainee() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        Trainee trainee = traineeRepository.findByUsername(createdTrainee.getUser().getUsername()).orElseThrow();
        boolean isAuthenticated = facade.isTraineeAuthenticated(createdTrainee.getUser().getUsername(), trainee.getUser().getPassword());
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldChangeTraineePassword() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        Trainee trainee = traineeRepository.findByUsername(createdTrainee.getUser().getUsername()).orElseThrow();
        facade.changeTraineePassword(createdTrainee.getUser().getUsername(), trainee.getUser().getPassword(), "newPassword");
        boolean isAuthenticated = facade.isTraineeAuthenticated(createdTrainee.getUser().getUsername(), "newPassword");
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldChangeTraineeStatus() {
        TraineeDto traineeDto = TestData.createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        boolean initialStatus = createdTrainee.getUser().getIsActive();
        boolean newStatus = facade.changeTraineeStatus(createdTrainee.getUser().getUsername());
        assertNotEquals(initialStatus, newStatus);
        boolean finalStatus = facade.changeTraineeStatus(createdTrainee.getUser().getUsername());
        assertNotEquals(newStatus, finalStatus);
    }

    @Test
    void shouldNotUpdateTrainingTypes() {
        TrainingType trainingType = trainingTypeRepository.findByName("Yoga").orElseThrow();
        trainingType.setName("ChangedName");
        TrainerDto trainer = TestData.createTrainerDto();

        trainer.setSpecialization(trainingType);
        facade.createTrainer(trainer);
        assertTrue(trainingTypeRepository.findByName("ChangedName").isEmpty());
    }

    @Test
    void shouldCreateTrainingWithLinkedTrainerAndTraineeAndFindByCriteria() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        TrainerDto trainerDto = createdTraining.getTrainer();
        TraineeDto traineeDto = createdTraining.getTrainee();

        TrainingSearchDto trainingSearchDto = new TrainingSearchDto();
        trainingSearchDto.setIsTrainee(true);
        trainingSearchDto.setUsername(traineeDto.getUser().getUsername());

        List<TrainingDto> result = facade.findAllTrainingsByCriteria(trainingSearchDto);

        assertEquals(1, result.size());
        assertEquals(createdTraining, result.get(0));
        assertEquals(traineeDto, result.get(0).getTrainee());
        assertEquals(trainerDto, result.get(0).getTrainer());
    }

    @Test
    void shouldGetUnassignedTrainersListByUsername() {
        TrainerDto trainerDto = createTrainerDto();
        TraineeDto traineeDto = createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        TrainerDto createdTrainer = facade.createTrainer(trainerDto);
        List<TrainerDto> result = facade.getUnassignedTrainersListByUsername(createdTrainee.getUser().getUsername());
        assertTrue(result.contains(createdTrainer));
    }

    @Test
    void shouldUpdateTrainersListByUsername() {
        TrainerDto trainerDto = createTrainerDto();
        TraineeDto traineeDto = createTraineeDto();
        TraineeDto createdTrainee = facade.createTrainee(traineeDto);
        TrainerDto createdTrainer = facade.createTrainer(trainerDto);
        List<String> trainerUsernames = Collections.singletonList(createdTrainer.getUser().getUsername());
        List<TrainerDto> result = facade.updateTrainersListByUsername(createdTrainee.getUser().getUsername(), trainerUsernames);
        assertEquals(1, result.size());
        assertEquals(createdTrainer, result.get(0));
    }

    @Test
    void shouldUpdateTrainer() {
        TrainerDto trainerDto = createTrainerDto();
        TrainerDto createdTrainer = facade.createTrainer(trainerDto);
        createdTrainer.getUser().setFirstName("Updated");
        facade.updateTrainer(createdTrainer);
        TrainerDto result = facade.getTrainerByUsername(createdTrainer.getUser().getUsername());
        assertEquals("Updated", result.getUser().getFirstName());
    }

    @Test
    void shouldCreateTraining() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        assertNotNull(createdTraining.getId());
    }

    @Test
    void shouldGetTrainingById() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        TrainingDto result = facade.getTrainingById(createdTraining.getId());
        assertEquals(createdTraining, result);
    }

    @Test
    void shouldAuthenticateTrainer() {
        TrainerDto trainerDto = TestData.createTrainerDto();
        TrainerDto createdTrainee = facade.createTrainer(trainerDto);
        Trainer trainer = trainerRepository.findByUsername(createdTrainee.getUser().getUsername()).orElseThrow();
        boolean isAuthenticated = facade.isTrainerAuthenticated(createdTrainee.getUser().getUsername(), trainer.getUser().getPassword());
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldChangeTrainerStatus() {
        TrainerDto trainerDto = TestData.createTrainerDto();
        TrainerDto createdTrainer = facade.createTrainer(trainerDto);
        boolean initialStatus = createdTrainer.getUser().getIsActive();
        boolean newStatus = facade.changeTrainerStatus(createdTrainer.getUser().getUsername());
        assertNotEquals(initialStatus, newStatus);
        boolean finalStatus = facade.changeTrainerStatus(createdTrainer.getUser().getUsername());
        assertNotEquals(newStatus, finalStatus);
    }

    @Test
    void shouldChangeTrainerPassword() {
        TrainerDto trainerDto = TestData.createTrainerDto();
        TrainerDto createdTrainer = facade.createTrainer(trainerDto);
        Trainer trainer = trainerRepository.findByUsername(createdTrainer.getUser().getUsername()).orElseThrow();
        facade.changeTrainerPassword(createdTrainer.getUser().getUsername(), trainer.getUser().getPassword(), "newPassword");
        boolean isAuthenticated = facade.isTrainerAuthenticated(createdTrainer.getUser().getUsername(), "newPassword");
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistentTrainee() {
        assertThrows(NoSuchElementException.class, () -> facade.deleteTraineeByUsername("nonexistent"));
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTrainee() {
        TraineeDto invalidTraineeDto = createTraineeDto();
        invalidTraineeDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTrainee(invalidTraineeDto));
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTrainer() {
        TrainerDto invalidTrainerDto = createTrainerDto();
        invalidTrainerDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTrainer(invalidTrainerDto));
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTraining() {
        TrainingDto invalidTrainingDto = createTrainingDto();
        invalidTrainingDto.setName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTraining(invalidTrainingDto));
    }

    @Test
    void shouldThrowErrorWhenUpdatingInvalidTrainee() {
        TraineeDto invalidTraineeDto = createTraineeDto();
        invalidTraineeDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.updateTrainee(invalidTraineeDto));
    }

    @Test
    void shouldThrowErrorWhenUpdatingInvalidTrainer() {
        TrainerDto invalidTrainerDto = createTrainerDto();
        invalidTrainerDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.updateTrainer(invalidTrainerDto));
    }

}

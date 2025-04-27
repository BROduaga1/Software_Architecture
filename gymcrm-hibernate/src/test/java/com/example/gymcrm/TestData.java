package com.example.gymcrm;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.domain.User;
import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.dto.TrainingDto;
import com.example.gymcrm.dto.TrainingSearchDto;
import com.example.gymcrm.dto.UserDto;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestData {

    public static final String TRAINEE_NOT_FOUND = "Trainee wasn't found with id:";
    public static final String TRAINER_NOT_FOUND = "Trainer wasn't found with id:";
    public static final String TRAINING_NOT_FOUND = "Training wasn't found with id:";
    private static final TrainingType TRAINER_SPECIALIZATION = new TrainingType(1L, "Yoga");
    private static final Long TRAINEE_ID = 1L;
    private static final String TRAINEE_FIRST_NAME = "john";
    private static final String TRAINEE_LAST_NAME = "doe";
    private static final String TRAINEE_USERNAME = TRAINEE_FIRST_NAME + "." + TRAINEE_LAST_NAME;
    private static final Boolean TRAINEE_ACTIVE = true;
    private static final String TRAINEE_ADDRESS = "address";
    private static final LocalDate TRAINEE_BIRTHDATE = LocalDate.of(1990, 1, 1);
    private static final String UPDATED_TRAINEE_FIRST_NAME = "updName";
    private static final String UPDATED_TRAINEE_ADDRESS = "updAddress";
    private static final Long TRAINER_ID = 6L;
    private static final String TRAINER_FIRST_NAME = "Mark";
    private static final String TRAINER_LAST_NAME = "Twain";
    private static final String TRAINER_USERNAME = TRAINER_FIRST_NAME + "." + TRAINER_LAST_NAME;
    private static final Boolean TRAINER_ACTIVE = true;
    private static final Long TRAINING_ID = 1L;
    private static final Long TRAINER_ID_FOR_TRAINING = 5L;
    private static final Long TRAINEE_ID_FOR_TRAINING = 5L;
    private static final String TRAINING_NAME = "Zumba session";
    private static final TrainingType TRAINING_TYPE = new TrainingType(1L, "Yoga");
    private static final LocalDateTime TRAINING_DATE = LocalDateTime.of(2021, 1, 1, 10, 0);
    private static final Long TRAINING_DURATION = 45L;
    private static final String TRAINEE_PASSWORD = "password";
    private static final String TRAINER_PASSWORD = "password_trainer";

    private static final String UPDATED_USERNAME = "Updated";

    public static TraineeDto createTraineeDto() {
        return TraineeDto.builder()
                .id(TRAINEE_ID)
                .dateOfBirth(TRAINEE_BIRTHDATE)
                .address(TRAINEE_ADDRESS)
                .user(UserDto.builder()
                        .id(TRAINEE_ID)
                        .firstName(TRAINEE_FIRST_NAME)
                        .lastName(TRAINEE_LAST_NAME)
                        .username(TRAINEE_USERNAME)
                        .isActive(TRAINEE_ACTIVE)
                        .build())
                .build();
    }

    public static TraineeDto createUpdatedTraineeDto() {
        return TraineeDto.builder()
                .id(TRAINEE_ID)
                .dateOfBirth(TRAINEE_BIRTHDATE)
                .address(UPDATED_TRAINEE_ADDRESS)
                .user(UserDto.builder()
                        .id(TRAINEE_ID)
                        .username(TRAINEE_USERNAME)
                        .isActive(TRAINEE_ACTIVE)
                        .build())
                .build();
    }

    public static Trainee createTrainee() {
        return Trainee.builder()
                .id(TRAINEE_ID)
                .dateOfBirth(TRAINEE_BIRTHDATE)
                .address(TRAINEE_ADDRESS)
                .user(User.builder()
                        .id(TRAINEE_ID)
                        .firstName(TRAINEE_FIRST_NAME)
                        .lastName(TRAINEE_LAST_NAME)
                        .username(TRAINEE_USERNAME)
                        .isActive(TRAINEE_ACTIVE)
                        .password(TRAINEE_PASSWORD)
                        .build())
                .build();
    }

    public static TrainerDto createTrainerDto() {
        return TrainerDto.builder()
                .id(TRAINER_ID)
                .specialization(TRAINER_SPECIALIZATION)
                .user(UserDto.builder()
                        .id(TRAINER_ID)
                        .firstName(TRAINER_FIRST_NAME)
                        .lastName(TRAINER_LAST_NAME)
                        .username(TRAINER_USERNAME)
                        .isActive(TRAINER_ACTIVE)
                        .build())
                .build();
    }

    public static TrainerDto createUpdatedTrainerDto() {
        return TrainerDto.builder()
                .id(TRAINER_ID)
                .specialization(TRAINER_SPECIALIZATION)
                .user(UserDto.builder()
                        .id(TRAINER_ID)
                        .firstName(UPDATED_TRAINEE_FIRST_NAME)
                        .lastName(TRAINER_LAST_NAME)
                        .username(TRAINER_USERNAME)
                        .isActive(TRAINER_ACTIVE)
                        .build())
                .build();
    }

    public static Trainer createTrainer() {
        return Trainer.builder()
                .id(TRAINER_ID)
                .specialization(TRAINER_SPECIALIZATION)
                .user(User.builder()
                        .id(TRAINER_ID)
                        .firstName(TRAINER_FIRST_NAME)
                        .lastName(TRAINER_LAST_NAME)
                        .username(TRAINER_USERNAME)
                        .password(TRAINER_PASSWORD)
                        .isActive(TRAINER_ACTIVE)
                        .build())
                .build();
    }

    public static TrainingDto createTrainingDto() {
        return TrainingDto.builder()
                .id(TRAINING_ID)
                .trainer(createTrainerDto())
                .trainee(createTraineeDto())
                .name(TRAINING_NAME)
                .type(TRAINING_TYPE)
                .date(TRAINING_DATE)
                .durationMinutes(TRAINING_DURATION)
                .build();
    }

    public static Training createTraining() {
        return Training.builder()
                .id(TRAINING_ID)
                .trainer(createTrainer())
                .trainee(createTrainee())
                .name(TRAINING_NAME)
                .type(TRAINING_TYPE)
                .date(TRAINING_DATE)
                .durationMinutes(TRAINING_DURATION)
                .build();
    }

    public static TrainingSearchDto createSearchDto(Training training, boolean isTrainee, boolean expectUsername, boolean expectFromDate, boolean expectToDate) {
        String username = isTrainee ? training.getTrainee().getUser().getUsername() : training.getTrainer().getUser().getUsername();
        return TrainingSearchDto.builder()
                .username(expectUsername ? username : "wrongUsername")
                .fromDate(expectFromDate ? training.getDate() : training.getDate().minusDays(1))
                .toDate(expectToDate ? training.getDate() : training.getDate().plusDays(1))
                .isTrainee(isTrainee)
                .build();
    }
}

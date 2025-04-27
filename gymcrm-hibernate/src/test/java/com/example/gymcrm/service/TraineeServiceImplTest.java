package com.example.gymcrm.service;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.TraineeDto;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.mapper.TraineeMapper;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.service.impl.TraineeServiceImpl;
import com.example.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.example.gymcrm.TestData.TRAINEE_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private UsernameGeneratorService usernameGeneratorService;
    @Mock
    TrainerService trainerService;
    @Mock
    TrainerMapper trainerMapper;

    private TraineeDto traineeDto;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeDto = TestData.createTraineeDto();
        trainee = TestData.createTrainee();
    }

    @Test
    void shouldSaveTraineeAndGeneratePasswordAndUsername() {
        when(traineeMapper.toEntity(traineeDto)).thenReturn(trainee);
        when(usernameGeneratorService.generateUsername(traineeDto.getUser().getFirstName(), traineeDto.getUser().getLastName())).thenReturn("username");
        when(passwordGenerator.generatePassword()).thenReturn("password");
        traineeService.create(traineeDto);

        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository, times(1)).save(traineeCaptor.capture());

        Trainee savedTrainee = traineeCaptor.getValue();
        assertThat(savedTrainee.getUser().getPassword()).isEqualTo("password");
        assertThat(savedTrainee.getUser().getUsername()).isEqualTo("username");
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTrainee() {
        assertThrows(NullPointerException.class, () -> traineeService.create(null));
    }

    @Test
    void shouldRetrieveTraineeByUsername() {
        when(traineeMapper.toDto(any())).thenReturn(traineeDto);
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        TraineeDto retrieved = traineeService.getByUsername(traineeDto.getUser().getUsername());

        assertNotNull(retrieved);
        assertEquals(traineeDto.getUser().getUsername(), retrieved.getUser().getUsername());
        verify(traineeRepository, times(1)).findByUsername(traineeDto.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFoundByUsername() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.empty());
        String username = traineeDto.getUser().getUsername();
        assertThrows(NoSuchElementException.class, () -> traineeService.getByUsername(username));
    }

    @Test
    void shouldUpdateTraineeByUsername() {
        TraineeDto updatedTraineeDto = TestData.createUpdatedTraineeDto();
        when(traineeRepository.findByUsername(updatedTraineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        traineeService.update(updatedTraineeDto);
        verify(traineeRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTrainee() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.empty());
        TraineeDto updatedTraineeDto = TestData.createUpdatedTraineeDto();
        assertThrows(NoSuchElementException.class, () -> traineeService.update(updatedTraineeDto));
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        traineeService.deleteByUsername(traineeDto.getUser().getUsername());
        verify(traineeRepository, times(1)).deleteByUsername(traineeDto.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTrainee() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.empty());
        String username = traineeDto.getUser().getUsername();
        assertThrows(NoSuchElementException.class, () -> traineeService.deleteByUsername(username), TRAINEE_NOT_FOUND + username);
    }

    @Test
    void shouldAuthenticateTrainee() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        boolean isAuthenticated = traineeService.isAuthenticated(traineeDto.getUser().getUsername(), trainee.getUser().getPassword());
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldChangeTraineePassword() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        traineeService.changePassword(traineeDto.getUser().getUsername(), "password", "newPassword");
        verify(traineeRepository, times(1)).save(trainee);
        assertEquals("newPassword", trainee.getUser().getPassword());
    }

    @Test
    void shouldChangeTraineeStatus() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        boolean newStatus = traineeService.changeStatus(traineeDto.getUser().getUsername());
        verify(traineeRepository, times(2)).save(trainee);
        assertFalse(newStatus);
    }

    @Test
    void shouldUpdateTrainersListByUsername() {
        when(traineeRepository.findByUsername(traineeDto.getUser().getUsername())).thenReturn(Optional.of(trainee));
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        List<TrainerDto> updatedTrainers = traineeService.updateTrainersListByUsername(traineeDto.getUser().getUsername(), trainerUsernames);
        verify(traineeRepository, times(1)).save(trainee);
        assertNotNull(updatedTrainers);
    }

    @Test
    void shouldThrowExceptionWhenChangingPasswordWithWrongLastPassword() {
        String username = traineeDto.getUser().getUsername();
        String wrongPassword = "wrongPassword";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        assertThrows(IllegalArgumentException.class, () -> traineeService.changePassword(username, wrongPassword, "newPassword"));
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFoundForAuthentication() {
        String username = traineeDto.getUser().getUsername();
        String password = trainee.getUser().getPassword();
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> traineeService.isAuthenticated(username, password));
    }
}

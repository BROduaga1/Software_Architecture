package com.example.gymcrm.service;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.TrainerDto;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.service.impl.TrainerServiceImpl;
import com.example.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private UsernameGeneratorService usernameGeneratorService;

    private TrainerDto trainerDto;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerDto = TestData.createTrainerDto();
        trainer = TestData.createTrainer();
    }

    @Test
    void shouldCreateTrainerSuccessfully() {
        when(trainerMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(usernameGeneratorService.generateUsername(trainerDto.getUser().getFirstName(), trainerDto.getUser().getLastName())).thenReturn("username");
        when(passwordGenerator.generatePassword()).thenReturn("password");
        trainerService.create(trainerDto);

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository, times(1)).save(trainerCaptor.capture());

        Trainer savedTrainer = trainerCaptor.getValue();
        assertThat(savedTrainer.getUser().getPassword()).isEqualTo("password");
        assertThat(savedTrainer.getUser().getUsername()).isEqualTo("username");
    }

    @Test
    void shouldThrowExceptionWhenCreatingNullTrainer() {
        assertThrows(NullPointerException.class, () -> trainerService.create(null));
    }

    @Test
    void shouldRetrieveTrainerByUsernameSuccessfully() {
        when(trainerMapper.toDto(any())).thenReturn(trainerDto);
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        TrainerDto retrieved = trainerService.getByUsername(trainerDto.getUser().getUsername());

        assertNotNull(retrieved);
        assertEquals(trainerDto.getUser().getUsername(), retrieved.getUser().getUsername());
        verify(trainerRepository, times(1)).findByUsername(trainerDto.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFoundByUsername() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.empty());
        String username = trainerDto.getUser().getUsername();
        assertThrows(NoSuchElementException.class, () -> trainerService.getByUsername(username));
    }

    @Test
    void shouldUpdateTrainerByUsernameSuccessfully() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        TrainerDto updatedTrainerDto = TestData.createUpdatedTrainerDto();
        trainerService.update(updatedTrainerDto);
        verify(trainerRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTrainer() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.empty());
        TrainerDto updatedTrainerDto = TestData.createUpdatedTrainerDto();
        assertThrows(NoSuchElementException.class, () -> trainerService.update(updatedTrainerDto));
    }

    @Test
    void shouldAuthenticateTrainer() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        boolean isAuthenticated = trainerService.isAuthenticated(trainerDto.getUser().getUsername(), trainer.getUser().getPassword());
        assertTrue(isAuthenticated);
    }

    @Test
    void shouldChangeTrainerPassword() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        trainerService.changePassword(trainerDto.getUser().getUsername(), trainer.getUser().getPassword(), "newPassword");
        verify(trainerRepository, times(1)).save(trainer);
        assertEquals("newPassword", trainer.getUser().getPassword());
    }

    @Test
    void shouldChangeTrainerStatus() {
        when(trainerRepository.findByUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        boolean newStatus = trainerService.changeStatus(trainerDto.getUser().getUsername());
        verify(trainerRepository, times(2)).save(trainer);
        assertFalse(newStatus);
    }

    @Test
    void shouldThrowExceptionWhenChangingPasswordWithIncorrectLastPassword() {
        String username = trainerDto.getUser().getUsername();
        String incorrectLastPassword = "incorrectPassword";
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        assertThrows(IllegalArgumentException.class, () -> trainerService.changePassword(username, incorrectLastPassword, "newPassword"));
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFoundForAuthentication() {
        String username = trainerDto.getUser().getUsername();
        String password = trainer.getUser().getPassword();
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> trainerService.isAuthenticated(username, password));
    }
}

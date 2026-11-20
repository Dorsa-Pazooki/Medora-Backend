package org.dorsa.medorabackend.BLL.Services;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.BLL.Domain.Schedule;
import org.dorsa.medorabackend.BLL.Interfaces.AvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityServiceTest {

    private AvailabilityRepository repository;
    private AvailabilityService service;

    @BeforeEach
    void setUp() {
        repository = mock(AvailabilityRepository.class);
        service = new AvailabilityService(repository);
    }

    @Test
    void shouldRegisterAvailability() {
        Long doctorId = 1L;

        when(repository.findByDoctorId(doctorId))
                .thenReturn(Optional.empty());

        Availability availability = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        service.registerAvailability(doctorId, availability);

        verify(repository).save(eq(doctorId), argThat(schedule ->
                schedule.getAvailabilities().size() == 1
        ));
    }

    @Test
    void shouldRejectOverlappingAvailability() {
        Long doctorId = 1L;

        Schedule schedule = new Schedule();
        schedule.addAvailability(new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        ));

        when(repository.findByDoctorId(doctorId))
                .thenReturn(Optional.of(schedule));

        Availability overlapping = new Availability(
                LocalTime.of(11, 0),
                LocalTime.of(14, 0),
                DayOfWeek.MONDAY
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.registerAvailability(doctorId, overlapping)
        );

        verify(repository, never()).save(anyLong(), any(Schedule.class));
    }

    @Test
    void shouldReturnEmptyListWhenScheduleDoesNotExist() {
        when(repository.findByDoctorId(1L))
                .thenReturn(Optional.empty());

        assertTrue(service.getAvailability(1L).isEmpty());
    }

    @Test
    void shouldUpdateAvailability() {
        Long doctorId = 1L;

        Availability oldPeriod = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        Availability newPeriod = new Availability(
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                DayOfWeek.MONDAY
        );

        Schedule schedule = new Schedule();
        schedule.addAvailability(oldPeriod);

        when(repository.findByDoctorId(doctorId))
                .thenReturn(Optional.of(schedule));

        service.updateAvailability(doctorId, oldPeriod, newPeriod);

        verify(repository).save(eq(doctorId), argThat(updated ->
                updated.getAvailabilities().size() == 1
                        && updated.getAvailabilities().get(0)
                        .getStartTime().equals(LocalTime.of(10, 0))
        ));
    }
}

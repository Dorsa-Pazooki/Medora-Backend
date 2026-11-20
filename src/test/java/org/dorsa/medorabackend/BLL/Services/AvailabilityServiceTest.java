
package org.dorsa.medorabackend.BLL.Services;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.DAL.Entities.AvailabilityEntity;
import org.dorsa.medorabackend.DAL.Repository.JpaAvailabilityRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityServiceTest {

    private JpaAvailabilityRepository repository;
    private AvailabilityService service;

    @BeforeEach
    void setUp() {
        repository = mock(JpaAvailabilityRepository.class);
        service = new AvailabilityService(repository);
    }

    @Test
    void shouldRegisterAvailability() {
        Long doctorId = 1L;

        when(repository.findByDoctorId(doctorId))
                .thenReturn(List.of());

        Availability availability = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        service.registerAvailability(doctorId, availability);

        verify(repository).save(argThat(entity ->
                entity.getDoctorId().equals(doctorId)
                        && entity.getDay() == DayOfWeek.MONDAY
                        && entity.getStartTime().equals(LocalTime.of(9, 0))
        ));
    }

    @Test
    void shouldRejectOverlappingAvailability() {
        Long doctorId = 1L;

        AvailabilityEntity existing = new AvailabilityEntity(
                doctorId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)
        );

        when(repository.findByDoctorId(doctorId))
                .thenReturn(List.of(existing));

        Availability overlapping = new Availability(
                LocalTime.of(11, 0),
                LocalTime.of(14, 0),
                DayOfWeek.MONDAY
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.registerAvailability(doctorId, overlapping)
        );

        verify(repository, never()).save(any(AvailabilityEntity.class));
    }

    @Test
    void shouldReturnEmptyListWhenScheduleDoesNotExist() {
        when(repository.findByDoctorId(1L))
                .thenReturn(List.of());

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

        AvailabilityEntity existing = new AvailabilityEntity(
                doctorId,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)
        );

        when(repository.findByDoctorId(doctorId))
                .thenReturn(List.of(existing));

        service.updateAvailability(doctorId, oldPeriod, newPeriod);

        verify(repository).delete(existing);

        verify(repository).save(argThat(entity ->
                entity.getDoctorId().equals(doctorId)
                        && entity.getStartTime().equals(LocalTime.of(10, 0))
                        && entity.getEndTime().equals(LocalTime.of(13, 0))
        ));
    }
}

package org.dorsa.medorabackend.Presentation.Controllers;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.BLL.Services.AvailabilityService;
import org.dorsa.medorabackend.Presentation.DTOs.AvailabilityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityControllerTest {

    private AvailabilityService service;
    private AvailabilityController controller;

    @BeforeEach
    void setUp() {
        service = mock(AvailabilityService.class);
        controller = new AvailabilityController(service);
    }

    @Test
    void shouldRegisterAvailability() {
        Long doctorId = 1L;

        AvailabilityRequest request = new AvailabilityRequest(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)
        );

        var response = controller.registerAvailability(doctorId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DayOfWeek.MONDAY, response.getBody().day());

        verify(service).registerAvailability(
                eq(doctorId),
                any(Availability.class)
        );
    }

    @Test
    void shouldGetAvailability() {
        Long doctorId = 1L;

        Availability availability = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        when(service.getAvailability(doctorId))
                .thenReturn(List.of(availability));

        var response = controller.getAvailability(doctorId);

        assertEquals(1, response.size());
        assertEquals(DayOfWeek.MONDAY, response.get(0).day());

        verify(service).getAvailability(doctorId);
    }

    @Test
    void shouldUpdateAvailability() {
        Long doctorId = 1L;

        AvailabilityRequest request = new AvailabilityRequest(
                DayOfWeek.MONDAY,
                LocalTime.of(10, 0),
                LocalTime.of(13, 0)
        );

        var response = controller.updateAvailability(
                doctorId,
                "MONDAY",
                "09:00",
                "12:00",
                request
        );

        assertEquals(LocalTime.of(10, 0), response.startTime());

        verify(service).updateAvailability(
                eq(doctorId),
                any(Availability.class),
                any(Availability.class)
        );
    }
}

package org.dorsa.medorabackend.Presentation.DTOs;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityResponse(
        DayOfWeek day,
        LocalTime startTime,
        LocalTime endTime
) {
}

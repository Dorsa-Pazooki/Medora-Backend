package org.dorsa.medorabackend.BLL.Domain;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    void shouldAddAvailability() {
        Schedule schedule = new Schedule();

        Availability availability = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        schedule.addAvailability(availability);

        assertEquals(1, schedule.getAvailabilities().size());
    }

    @Test
    void shouldRejectOverlappingAvailability() {
        Schedule schedule = new Schedule();

        Availability first = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        Availability second = new Availability(
                LocalTime.of(11, 0),
                LocalTime.of(14, 0),
                DayOfWeek.MONDAY
        );

        schedule.addAvailability(first);

        assertThrows(IllegalArgumentException.class, () -> {
            schedule.addAvailability(second);
        });

        assertEquals(1, schedule.getAvailabilities().size());
    }

    @Test
    void shouldAllowNonOverlappingAvailability() {
        Schedule schedule = new Schedule();

        schedule.addAvailability(new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        ));

        schedule.addAvailability(new Availability(
                LocalTime.of(13, 0),
                LocalTime.of(17, 0),
                DayOfWeek.MONDAY
        ));

        assertEquals(2, schedule.getAvailabilities().size());
    }

    @Test
    void shouldAllowSameTimeOnDifferentDays() {
        Schedule schedule = new Schedule();

        schedule.addAvailability(new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        ));

        schedule.addAvailability(new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.TUESDAY
        ));

        assertEquals(2, schedule.getAvailabilities().size());
    }

    @Test
    void shouldRemoveAvailability() {
        Schedule schedule = new Schedule();

        Availability availability = new Availability(
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                DayOfWeek.MONDAY
        );

        schedule.addAvailability(availability);
        schedule.removeAvailability(availability);

        assertTrue(schedule.getAvailabilities().isEmpty());
    }
}

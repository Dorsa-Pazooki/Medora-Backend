package org.dorsa.medorabackend.BLL.Domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AvailabilityTest {

    @Test
    void startTimeISBeforeEndTime (){
        assertThrows(IllegalArgumentException.class, () -> {
            new Availability (
                    LocalTime.of(17,0),
                    LocalTime.of(9,0),
                    DayOfWeek.MONDAY
            );
        });
    }

    @Test
    void startTimeIsBeforeEndTime(){
        assertDoesNotThrow( () -> {
            new Availability (
                    LocalTime.of(9,0),
                    LocalTime.of(17,0),
                    DayOfWeek.MONDAY
            );
        });
    }

    @Test
    void shouldRejectEqualStartTimeAndEndTime(){
        assertThrows(IllegalArgumentException.class, () ->{
            new Availability (
                    LocalTime.of(9,0),
                    LocalTime.of(9,0),
                    DayOfWeek.MONDAY
            );
        });
    }

    @Test
    void shouldRejectNullStartTime(){
        assertThrows(IllegalArgumentException.class, () ->{
            new Availability (
                    null,
                    LocalTime.of(12,0),
                    DayOfWeek.MONDAY
            );
        });
    }

    @Test
    void shouldRejectNullEndTime(){
        assertThrows(IllegalArgumentException.class, () ->{
            new Availability (
                    LocalTime.of(9,0),
                    null,
                    DayOfWeek.MONDAY
            );
        });
    }

    @Test
    void shouldRejectNullDayOfWeek(){
        assertThrows(IllegalArgumentException.class, () ->{
            new Availability (
                    LocalTime.of(9,0),
                    LocalTime.of(12,0),
                    null
            );
        });
    }
}

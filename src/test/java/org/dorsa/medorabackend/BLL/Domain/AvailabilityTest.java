package org.dorsa.medorabackend.BLL.Domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}

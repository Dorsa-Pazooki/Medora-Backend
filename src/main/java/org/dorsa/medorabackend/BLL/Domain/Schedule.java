package org.dorsa.medorabackend.BLL.Domain;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private final List<Availability> availabilities = new ArrayList<>();

    public void addAvailability(Availability newAvailability) {

        if (newAvailability == null) {
            throw new IllegalArgumentException("Availability cannot be null");
        }

        for (Availability existing : availabilities) {
            if (existing.overlapsWith(newAvailability)) {
                throw new IllegalArgumentException(
                        "Availability overlaps with an existing period"
                );
            }
        }

        availabilities.add(newAvailability);
    }

    public void removeAvailability(Availability availability) {

        if (availability == null) {
            throw new IllegalArgumentException("Availability cannot be null");
        }

        boolean removed = availabilities.removeIf(existing ->
                existing.getDay() == availability.getDay()
                        && existing.getStartTime().equals(availability.getStartTime())
                        && existing.getEndTime().equals(availability.getEndTime())
        );

        if (!removed) {
            throw new IllegalArgumentException("Availability period not found");
        }
    }

    public List<Availability> getAvailabilities() {
        return List.copyOf(availabilities);
    }
}

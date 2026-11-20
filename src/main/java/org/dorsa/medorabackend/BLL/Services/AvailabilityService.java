package org.dorsa.medorabackend.BLL.Services;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.BLL.Domain.Schedule;
import org.dorsa.medorabackend.BLL.Interfaces.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository repository;

    public AvailabilityService(AvailabilityRepository repository) {
        this.repository = repository;
    }

    public List<Availability> getAvailability(Long doctorId) {
        validateDoctorId(doctorId);

        return repository.findByDoctorId(doctorId)
                .map(Schedule::getAvailabilities)
                .orElseGet(List::of);
    }

    public void registerAvailability(Long doctorId, Availability availability) {
        validateDoctorId(doctorId);

        Schedule schedule = repository.findByDoctorId(doctorId)
                .orElseGet(Schedule::new);

        schedule.addAvailability(availability);
        repository.save(doctorId, schedule);
    }

    public void updateAvailability(
            Long doctorId,
            Availability oldAvailability,
            Availability newAvailability
    ) {
        validateDoctorId(doctorId);

        Schedule existing = repository.findByDoctorId(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Schedule not found")
                );

        // Work on a copy so a failed update does not change the loaded schedule.
        Schedule updated = new Schedule();

        for (Availability period : existing.getAvailabilities()) {
            updated.addAvailability(period);
        }

        updated.removeAvailability(oldAvailability);
        updated.addAvailability(newAvailability);

        repository.save(doctorId, updated);
    }

    private void validateDoctorId(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException("Invalid doctor ID");
        }
    }
}


package org.dorsa.medorabackend.BLL.Services;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.BLL.Domain.Schedule;
import org.dorsa.medorabackend.DAL.Entities.AvailabilityEntity;
import org.dorsa.medorabackend.DAL.Repository.JpaAvailabilityRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvailabilityService {

    private final JpaAvailabilityRepository repository;

    public AvailabilityService(JpaAvailabilityRepository repository) {
        this.repository = repository;
    }

    public List<Availability> getAvailability(Long doctorId) {
        validateDoctorId(doctorId);

        return repository.findByDoctorId(doctorId)
                .stream()
                .map(entity -> new Availability(
                        entity.getStartTime(),
                        entity.getEndTime(),
                        entity.getDay()
                ))
                .toList();
    }

    @Transactional
    public void registerAvailability(
            Long doctorId,
            Availability availability
    ) {
        validateDoctorId(doctorId);

        if (availability == null) {
            throw new IllegalArgumentException(
                    "Availability cannot be null"
            );
        }

        Schedule schedule = loadSchedule(doctorId);

        schedule.addAvailability(availability);

        AvailabilityEntity entity = new AvailabilityEntity(
                doctorId,
                availability.getDay(),
                availability.getStartTime(),
                availability.getEndTime()
        );

        repository.save(entity);
    }

    @Transactional
    public void updateAvailability(
            Long doctorId,
            Availability oldAvailability,
            Availability newAvailability
    ) {
        validateDoctorId(doctorId);

        if (oldAvailability == null || newAvailability == null) {
            throw new IllegalArgumentException(
                    "Availability cannot be null"
            );
        }

        Schedule schedule = loadSchedule(doctorId);

        // Check that the old period exists.
        schedule.removeAvailability(oldAvailability);

        // Check that the new period does not overlap.
        schedule.addAvailability(newAvailability);

        // Find the existing database record.
        AvailabilityEntity existingEntity =
                repository.findByDoctorId(doctorId)
                        .stream()
                        .filter(entity ->
                                entity.getDay() == oldAvailability.getDay()
                                        && entity.getStartTime().equals(
                                        oldAvailability.getStartTime()
                                )
                                        && entity.getEndTime().equals(
                                        oldAvailability.getEndTime()
                                )
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Availability not found"
                                )
                        );

        // Remove the old record.
        repository.delete(existingEntity);

        // Save the new record.
        AvailabilityEntity updatedEntity = new AvailabilityEntity(
                doctorId,
                newAvailability.getDay(),
                newAvailability.getStartTime(),
                newAvailability.getEndTime()
        );

        repository.save(updatedEntity);
    }

    private Schedule loadSchedule(Long doctorId) {
        Schedule schedule = new Schedule();

        List<AvailabilityEntity> entities =
                repository.findByDoctorId(doctorId);

        for (AvailabilityEntity entity : entities) {

            Availability availability = new Availability(
                    entity.getStartTime(),
                    entity.getEndTime(),
                    entity.getDay()
            );

            schedule.addAvailability(availability);
        }

        return schedule;
    }

    private void validateDoctorId(Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid doctor ID"
            );
        }
    }
}

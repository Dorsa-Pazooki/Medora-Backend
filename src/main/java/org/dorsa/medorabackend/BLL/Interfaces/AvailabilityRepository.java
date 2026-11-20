package org.dorsa.medorabackend.BLL.Interfaces;

import org.dorsa.medorabackend.BLL.Domain.Schedule;

import java.util.Optional;

public interface AvailabilityRepository {

    Optional<Schedule> findByDoctorId(Long doctorId);

    void save(Long doctorId, Schedule schedule);
}

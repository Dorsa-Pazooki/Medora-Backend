package org.dorsa.medorabackend.DAL.Repository;

import org.dorsa.medorabackend.DAL.Entities.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAvailabilityRepository
        extends JpaRepository<AvailabilityEntity, Long> {

    List<AvailabilityEntity> findByDoctorId(Long doctorId);
}
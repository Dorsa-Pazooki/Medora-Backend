package org.dorsa.medorabackend.Presentation.Controllers;

import org.dorsa.medorabackend.BLL.Domain.Availability;
import org.dorsa.medorabackend.BLL.Services.AvailabilityService;
import org.dorsa.medorabackend.Presentation.DTOs.AvailabilityRequest;
import org.dorsa.medorabackend.Presentation.DTOs.AvailabilityResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/availability")
public class AvailabilityController {

    private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    @GetMapping
    public List<AvailabilityResponse> getAvailability(
            @PathVariable Long doctorId
    ) {
        return service.getAvailability(doctorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<AvailabilityResponse> registerAvailability(
            @PathVariable Long doctorId,
            @RequestBody AvailabilityRequest request
    ) {
        Availability availability = toDomain(request);

        service.registerAvailability(doctorId, availability);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(availability));
    }

    @PutMapping
    public AvailabilityResponse updateAvailability(
            @PathVariable Long doctorId,
            @RequestParam String oldDay,
            @RequestParam String oldStartTime,
            @RequestParam String oldEndTime,
            @RequestBody AvailabilityRequest request
    ) {
        Availability oldAvailability = new Availability(
                java.time.LocalTime.parse(oldStartTime),
                java.time.LocalTime.parse(oldEndTime),
                java.time.DayOfWeek.valueOf(oldDay.toUpperCase())
        );

        Availability newAvailability = toDomain(request);

        service.updateAvailability(
                doctorId,
                oldAvailability,
                newAvailability
        );

        return toResponse(newAvailability);
    }

    private Availability toDomain(AvailabilityRequest request) {
        return new Availability(
                request.startTime(),
                request.endTime(),
                request.day()
        );
    }

    private AvailabilityResponse toResponse(Availability availability) {
        return new AvailabilityResponse(
                availability.getDay(),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }
}

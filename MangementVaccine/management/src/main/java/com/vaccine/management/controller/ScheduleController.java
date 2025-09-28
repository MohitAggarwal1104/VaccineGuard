package com.vaccine.management.controller;

import com.vaccine.management.dto.ScheduleDto;
import com.vaccine.management.model.ScheduleStatus;
import com.vaccine.management.model.Vaccine;
import com.vaccine.management.model.VaccineSchedule;
import com.vaccine.management.repository.ChildRepository;
import com.vaccine.management.repository.VaccineRepository;
import com.vaccine.management.repository.VaccineScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final VaccineScheduleRepository scheduleRepository;
    private final ChildRepository childRepository;
    private final VaccineRepository vaccineRepository;
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    public ScheduleController(VaccineScheduleRepository scheduleRepository, ChildRepository childRepository, VaccineRepository vaccineRepository) {
        this.scheduleRepository = scheduleRepository;
        this.childRepository = childRepository;
        this.vaccineRepository = vaccineRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleDto scheduleDto) {
        log.info("Attempting to create a schedule for child ID: {}", scheduleDto.getChildId());
        if (!childRepository.existsById(scheduleDto.getChildId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Child not found.");
        }

        Optional<Vaccine> vaccineOpt = vaccineRepository.findById(scheduleDto.getVaccineId());
        if (vaccineOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaccine not found.");
        }

        Vaccine vaccine = vaccineOpt.get();
        VaccineSchedule newSchedule = new VaccineSchedule(
                scheduleDto.getChildId(),
                vaccine.getId(),
                vaccine.getName(),
                LocalDate.parse(scheduleDto.getScheduledDate()),
                ScheduleStatus.PENDING
        );

        scheduleRepository.save(newSchedule);
        log.info("Successfully created schedule for child ID: {}", scheduleDto.getChildId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newSchedule);
    }

    @PutMapping("/{scheduleId}/complete")
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<?> markAsComplete(@PathVariable String scheduleId) {
        log.info("Attempting to mark schedule ID: {} as complete.", scheduleId);
        
        Optional<VaccineSchedule> scheduleOptional = scheduleRepository.findById(scheduleId);

        if (scheduleOptional.isPresent()) {
            VaccineSchedule schedule = scheduleOptional.get();
            schedule.setStatus(ScheduleStatus.COMPLETED);
            scheduleRepository.save(schedule);
            log.info("Successfully marked schedule ID: {} as complete.", scheduleId);
            return ResponseEntity.ok(schedule);
        } else {
            log.warn("Failed to update schedule. Schedule not found with ID: {}", scheduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found.");
        }
    }
}


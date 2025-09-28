package com.vaccine.management.controller;

import com.vaccine.management.model.Vaccine;
import com.vaccine.management.repository.VaccineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccines")
public class VaccineController {

    private final VaccineRepository vaccineRepository;
    private static final Logger log = LoggerFactory.getLogger(VaccineController.class);

    public VaccineController(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_DOCTOR', 'ROLE_PARENT')")
    public List<Vaccine> getAllVaccines() {
        log.info("Fetching all available vaccines.");
        return vaccineRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<?> createVaccine(@RequestBody Vaccine vaccine) {
        log.info("Attempting to create a new vaccine: {}", vaccine.getName());
        if (vaccineRepository.findByName(vaccine.getName()).isPresent()) {
            log.warn("Vaccine creation failed. A vaccine with name '{}' already exists.", vaccine.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A vaccine with this name already exists.");
        }
        Vaccine savedVaccine = vaccineRepository.save(vaccine);
        log.info("Successfully created vaccine: {}", savedVaccine.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVaccine);
    }
}


package com.vaccine.management.controller;

import com.vaccine.management.dto.ChildDto;
import com.vaccine.management.dto.ScheduleDto;
import com.vaccine.management.model.Child;
import com.vaccine.management.repository.ChildRepository;
import com.vaccine.management.repository.UserRepository;
import com.vaccine.management.repository.VaccineScheduleRepository;
import com.vaccine.management.security.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildRepository childRepository;
    private final UserRepository userRepository;
    private final VaccineScheduleRepository vaccineScheduleRepository;
    private static final Logger log = LoggerFactory.getLogger(ChildController.class);

    public ChildController(ChildRepository childRepository, UserRepository userRepository, VaccineScheduleRepository vaccineScheduleRepository) {
        this.childRepository = childRepository;
        this.userRepository = userRepository;
        this.vaccineScheduleRepository = vaccineScheduleRepository;
    }

    // Endpoint for doctors to get all children they have added
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<List<ChildDto>> getAllChildrenForDoctor() {
        String doctorId = getCurrentUserId();
        log.info("Doctor {} fetching all children records.", doctorId);
        List<Child> children = childRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(children.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    // Endpoint for parents to get only their own children
    @GetMapping("/my-children")
    @PreAuthorize("hasAuthority('ROLE_PARENT')")
    public ResponseEntity<List<ChildDto>> getMyChildren() {
        String parentId = getCurrentUserId();
        log.info("Parent {} fetching their children records.", parentId);
        List<Child> children = childRepository.findByParentId(parentId);
        return ResponseEntity.ok(children.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_DOCTOR')")
    public ResponseEntity<?> addChild(@RequestBody ChildDto childDto) {
        String doctorId = getCurrentUserId();
        log.info("Doctor {} attempting to add a new child: {}", doctorId, childDto.getName());

        if (!userRepository.existsById(childDto.getParentId())) {
            log.warn("Doctor {} failed to add child. Parent with ID {} not found.", doctorId, childDto.getParentId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parent with the provided ID does not exist.");
        }

        Child child = new Child(childDto.getName(), LocalDate.parse(childDto.getDob()), childDto.getParentId(), doctorId);
        childRepository.save(child);
        log.info("Doctor {} successfully added child {}.", doctorId, child.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(child));
    }

    // Helper method to get current user's ID from security context
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetailsService.CustomUserDetails userDetails = (MyUserDetailsService.CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    // Helper method to convert Child entity to ChildDto
    private ChildDto convertToDto(Child child) {
        List<ScheduleDto> scheduleDtos = vaccineScheduleRepository.findByChildId(child.getId()).stream()
                .map(schedule -> new ScheduleDto(
                        schedule.getId(),
                        schedule.getChildId(),
                        schedule.getVaccineId(),
                        schedule.getVaccineName(),
                        schedule.getScheduledDate().toString(),
                        schedule.getStatus().name()
                ))
                .collect(Collectors.toList());

        return new ChildDto(
                child.getId(),
                child.getName(),
                child.getDob().toString(),
                child.getParentId(),
                child.getDoctorId(),
                scheduleDtos
        );
    }
}


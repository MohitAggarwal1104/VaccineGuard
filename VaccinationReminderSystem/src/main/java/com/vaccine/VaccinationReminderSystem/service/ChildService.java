package com.vaccine.VaccinationReminderSystem.service;

import com.vaccine.VaccinationReminderSystem.dto.ChildDTO;
import com.vaccine.VaccinationReminderSystem.dto.ScheduleDTO;
import com.vaccine.VaccinationReminderSystem.model.Child;
import com.vaccine.VaccinationReminderSystem.model.Schedule;
import com.vaccine.VaccinationReminderSystem.model.User;
import com.vaccine.VaccinationReminderSystem.model.Vaccine;
import com.vaccine.VaccinationReminderSystem.repository.ChildRepository;
import com.vaccine.VaccinationReminderSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTANT IMPORT

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChildService(ChildRepository childRepository, UserRepository userRepository) {
        this.childRepository = childRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional
    public Child createChildForUser(Child child, String userEmail) {
        User parent = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        child.setParent(parent);
        return childRepository.save(child);
    }

    // This method is read-only, so @Transactional is optional but good practice
    @Transactional(readOnly = true)
    public List<ChildDTO> getChildrenForUser(String parentEmail) {
        List<Child> children = childRepository.findByParentEmail(parentEmail);

        return children.stream().map(this::convertToChildDTO).collect(Collectors.toList());
    }
    private ChildDTO convertToChildDTO(Child child) {
        ChildDTO childDTO = new ChildDTO();
        childDTO.setId(child.getId());
        childDTO.setName(child.getName());

        if (child.getDob() != null) {
            childDTO.setDob(child.getDob().toString());
        }

        // Get the parent from the child object and set the email on the DTO
        if (child.getParent() != null) {
            childDTO.setParentEmail(child.getParent().getEmail());
        }

        // Map schedules
        List<ScheduleDTO> scheduleDTOs = child.getSchedules().stream()
                                              .map(this::convertToScheduleDTO)
                                              .collect(Collectors.toList());
        childDTO.setSchedules(scheduleDTOs);
        
        return childDTO;
    }

    // ... rest of your service class

    // --- Helper method to convert Schedule to DTO ---
    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setCompleted(schedule.isCompleted());

        Date scheduledDate = schedule.getScheduledDate();
        if (scheduledDate != null) {
            scheduleDTO.setScheduledDate(scheduledDate.toString());
        }

        Vaccine vaccine = schedule.getVaccine();
        if (vaccine != null) {
            scheduleDTO.setVaccineName(vaccine.getName());
        }

        return scheduleDTO;
    }
    
    public Child getById(Long id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found with id: " + id));
    }
}
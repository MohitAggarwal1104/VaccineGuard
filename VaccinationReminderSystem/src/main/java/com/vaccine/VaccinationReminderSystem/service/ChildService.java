//package com.vaccine.VaccinationReminderSystem.service;
//
//import com.vaccine.VaccinationReminderSystem.model.Child;
//import com.vaccine.VaccinationReminderSystem.model.Schedule;
//import com.vaccine.VaccinationReminderSystem.model.User;
//import com.vaccine.VaccinationReminderSystem.model.Vaccine;
//import com.vaccine.VaccinationReminderSystem.dto.ChildDTO;
//import com.vaccine.VaccinationReminderSystem.dto.ScheduleDTO;
//import com.vaccine.VaccinationReminderSystem.repository.ChildRepository;
//import com.vaccine.VaccinationReminderSystem.repository.UserRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ChildService {
//
//    @Autowired
//    private ChildRepository childRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Format Date to String for DTO
//    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
//
//    // Get all children
//    public List<Child> getAll() {
//        return childRepository.findAll();
//    }
//
//    // Get child by ID
//    public Child getById(Long id) {
//        return childRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Child not found"));
//    }
//
//    // Save child (general save, not linked to user)
//    public Child save(Child child) {
//        return childRepository.save(child);
//    }
//
//    // Delete child
//    public void delete(Long id) {
//        childRepository.deleteById(id);
//    }
//
//    // ✅ Create child and link with logged-in user
//    public Child createChildForUser(Child child, String userEmail) {
//        // Find the User/Parent entity from your database using the email
//        User parent = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Associate the parent with the child
//        child.setParent(parent);
//
//        // Save the child
//        return childRepository.save(child);
//    }
//
//    // ✅ Get children for a specific parent (returns DTO)
//    public List<ChildDTO> getChildrenForUser(String parentEmail) {
//        List<Child> children = childRepository.findByParentEmail(parentEmail);
//
//        return children.stream().map(child -> {
//            ChildDTO childDTO = new ChildDTO();
//            childDTO.setId(child.getId());
//            childDTO.setName(child.getName());
//
//            // Convert java.util.Date to String
//            LocalDate dob = child.getDob();
//            if (dob != null) {
//                childDTO.setDob(dateFormatter.format(dob));
//            }
//
//            // Map schedules
//            List<ScheduleDTO> scheduleDTOs = child.getSchedules().stream().map(schedule -> {
//                ScheduleDTO scheduleDTO = new ScheduleDTO();
//                scheduleDTO.setId(schedule.getId());
//                scheduleDTO.setCompleted(schedule.isCompleted());
//
//                // Convert java.util.Date to String
//                Date scheduledDate = schedule.getScheduledDate();
//                if (scheduledDate != null) {
//                    scheduleDTO.setScheduledDate(dateFormatter.format(scheduledDate));
//                }
//
//                // Map vaccine name
//                Vaccine vaccine = schedule.getVaccine();
//                if (vaccine != null) {
//                    scheduleDTO.setVaccineName(vaccine.getName());
//                }
//
//                return scheduleDTO;
//            }).collect(Collectors.toList());
//
//            childDTO.setSchedules(scheduleDTOs);
//            return childDTO;
//        }).collect(Collectors.toList());
//    }
//}

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

    // 1. Use final fields for dependencies
    private final ChildRepository childRepository;
    private final UserRepository userRepository;

    // 2. Use Constructor Injection (Best Practice)
    @Autowired
    public ChildService(ChildRepository childRepository, UserRepository userRepository) {
        this.childRepository = childRepository;
        this.userRepository = userRepository;
    }

    // 3. Add @Transactional to ensure data is saved correctly
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

//    // --- Helper method to convert Child to DTO ---
//    private ChildDTO convertToChildDTO(Child child) {
//        ChildDTO childDTO = new ChildDTO();
//        childDTO.setId(child.getId());
//        childDTO.setName(child.getName());
//
//        // 4. FIX: Convert LocalDate to String correctly.
//        //    LocalDate.toString() automatically formats to "yyyy-MM-dd".
//        //    No SimpleDateFormat is needed!
//        LocalDate dob = child.getDob();
//        if (dob != null) {
//            childDTO.setDob(dob.toString());
//        }
//
//        // Map schedules
//        List<ScheduleDTO> scheduleDTOs = child.getSchedules().stream()
//                                              .map(this::convertToScheduleDTO)
//                                              .collect(Collectors.toList());
//        childDTO.setSchedules(scheduleDTOs);
//        
//        return childDTO;
//    }
 // ... inside your ChildService class

    private ChildDTO convertToChildDTO(Child child) {
        ChildDTO childDTO = new ChildDTO();
        childDTO.setId(child.getId());
        childDTO.setName(child.getName());

        if (child.getDob() != null) {
            childDTO.setDob(child.getDob().toString());
        }

        // --- ADD THIS LOGIC ---
        // Get the parent from the child object and set the email on the DTO
        if (child.getParent() != null) {
            childDTO.setParentEmail(child.getParent().getEmail());
        }
        // ----------------------

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

        // 5. FIX: Assume Schedule also uses LocalDate and convert it correctly.
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
    
    // Your other methods like getById, delete, etc., can remain here if you need them.
    public Child getById(Long id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Child not found with id: " + id));
    }
}
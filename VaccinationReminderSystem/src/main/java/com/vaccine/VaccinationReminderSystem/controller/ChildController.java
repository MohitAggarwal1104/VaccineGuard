package com.vaccine.VaccinationReminderSystem.controller;

import com.vaccine.VaccinationReminderSystem.dto.ChildDTO; // Import the DTO
import com.vaccine.VaccinationReminderSystem.model.Child;
import com.vaccine.VaccinationReminderSystem.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Import this
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child")
public class ChildController {
    @Autowired
    private ChildService childService;

 // MODIFY YOUR GET MAPPING FOR ALL CHILDREN
    @GetMapping
    public ResponseEntity<List<ChildDTO>> getChildrenForCurrentUser(Authentication authentication) {
        // Get the logged-in user's email from the security context
        String email = authentication.getName(); 
        
        // ✅ ADD THIS LINE FOR DEBUGGING
        System.out.println(">>>> FETCHING CHILDREN FOR LOGGED-IN USER: " + email);
        // Call the new service method that returns DTOs
        List<ChildDTO> children = childService.getChildrenForUser(email);
        
        return ResponseEntity.ok(children);
    }
    @GetMapping("/{id}")
    public Child getById(@PathVariable Long id) {
        return childService.getById(id);
    }
    @PostMapping
    public ResponseEntity<Child> create(@RequestBody Child child, Authentication authentication) {
        // 1. Get the email of the currently logged-in user.
        String email = authentication.getName();
        
        // 2. Pass the child data and the user's email to the service layer to handle the logic.
        Child savedChild = childService.createChildForUser(child, email);
        
        // 3. Return the saved child with a 200 OK status.
        return ResponseEntity.ok(savedChild);
    }
}

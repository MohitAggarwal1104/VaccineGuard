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

    @GetMapping
    public ResponseEntity<List<ChildDTO>> getChildrenForCurrentUser(Authentication authentication) {
        String email = authentication.getName(); 
        System.out.println(">>>> FETCHING CHILDREN FOR LOGGED-IN USER: " + email);
        List<ChildDTO> children = childService.getChildrenForUser(email);
        
        return ResponseEntity.ok(children);
    }
    @GetMapping("/{id}")
    public Child getById(@PathVariable Long id) {
        return childService.getById(id);
    }
    @PostMapping
    public ResponseEntity<Child> create(@RequestBody Child child, Authentication authentication) {
        String email = authentication.getName();
        Child savedChild = childService.createChildForUser(child, email);
        return ResponseEntity.ok(savedChild);
    }
}

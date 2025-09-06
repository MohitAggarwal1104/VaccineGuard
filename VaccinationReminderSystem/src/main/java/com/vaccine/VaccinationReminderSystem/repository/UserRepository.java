package com.vaccine.VaccinationReminderSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaccine.VaccinationReminderSystem.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by their email address
    Optional<User> findByEmail(String email);
}

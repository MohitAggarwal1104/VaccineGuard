package com.vaccine.VaccinationReminderSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaccine.VaccinationReminderSystem.model.Vaccine;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
}


package com.vaccine.VaccinationReminderSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaccine.VaccinationReminderSystem.model.Vaccine;
import com.vaccine.VaccinationReminderSystem.repository.VaccineRepository;

import java.util.List;

@Service
public class VaccineService {
    @Autowired
    private VaccineRepository vaccineRepository;

    public List<Vaccine> getAll() {
        return vaccineRepository.findAll();
    }
    public Vaccine getById(Long id) {
        return vaccineRepository.findById(id).orElseThrow(() -> new RuntimeException("Vaccine not found"));
    }
    public Vaccine save(Vaccine vaccine) {
        return vaccineRepository.save(vaccine);
    }
    public void delete(Long id) {
        vaccineRepository.deleteById(id);
    }
}
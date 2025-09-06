package com.vaccine.VaccinationReminderSystem.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.vaccine.VaccinationReminderSystem.model.Vaccine;
import com.vaccine.VaccinationReminderSystem.service.VaccineService;

import java.util.List;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {
    @Autowired
    private VaccineService vaccineService;

    @GetMapping
    public List<Vaccine> getAll() {
        return vaccineService.getAll();
    }
}
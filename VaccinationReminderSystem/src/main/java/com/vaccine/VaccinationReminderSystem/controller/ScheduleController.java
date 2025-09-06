package com.vaccine.VaccinationReminderSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vaccine.VaccinationReminderSystem.model.Schedule;
import com.vaccine.VaccinationReminderSystem.service.MongoLogService;
import com.vaccine.VaccinationReminderSystem.service.ScheduleService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private MongoLogService mongoLogService;

    @GetMapping("/child/{childId}")
    public List<Schedule> getByChild(@PathVariable Long childId) {
        return scheduleService.getByChildId(childId);
    }
    @PostMapping
    public Schedule create(@RequestBody Schedule schedule) {
        Schedule saved = scheduleService.save(schedule);
        mongoLogService.logScheduleEvent("CREATE", saved);
        return saved;
    }
 // Mark schedule as completed
    @PutMapping("/{id}")
    public ResponseEntity<Schedule> markCompleted(@PathVariable Long id) {
        Schedule updatedSchedule = scheduleService.markCompleted(id);
        return ResponseEntity.ok(updatedSchedule);
    }

}


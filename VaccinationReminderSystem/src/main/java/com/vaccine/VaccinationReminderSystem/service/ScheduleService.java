package com.vaccine.VaccinationReminderSystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaccine.VaccinationReminderSystem.model.Schedule;
import com.vaccine.VaccinationReminderSystem.repository.ScheduleRepository;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }
    public List<Schedule> getByChildId(Long childId) {
        return scheduleRepository.findByChildId(childId);
    }
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
    public Schedule getById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + id));
    }
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }
    public List<Schedule> getSchedulesForChild(Long childId) {
        return scheduleRepository.findByChildIdWithVaccine(childId);
    }
	 @Transactional
	    public Schedule markCompleted(Long id) {
	        Schedule schedule = scheduleRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Schedule not found"));

	        schedule.setCompleted(true);
	        return scheduleRepository.save(schedule);
	    }
}
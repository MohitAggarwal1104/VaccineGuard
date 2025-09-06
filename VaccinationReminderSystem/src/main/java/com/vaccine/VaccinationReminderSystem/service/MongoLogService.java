package com.vaccine.VaccinationReminderSystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.vaccine.VaccinationReminderSystem.model.Schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MongoLogService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void logScheduleEvent(String eventType, Schedule schedule) {
        Map<String, Object> log = new HashMap<>();
        log.put("eventType", eventType);
        log.put("childId", schedule.getChild().getId());
        log.put("childName", schedule.getChild().getName());
        log.put("vaccineName", schedule.getVaccine().getName());
        log.put("scheduledDate", schedule.getScheduledDate());
        log.put("isCompleted", schedule.isCompleted());
        log.put("timestamp", new Date());
        mongoTemplate.save(log, "vaccineLogs");
    }
}
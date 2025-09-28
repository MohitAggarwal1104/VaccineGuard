package com.vaccine.management.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "schedules")
public class VaccineSchedule {
    @Id
    private String id;
    private String childId;
    private String vaccineId;
    private String vaccineName; // Denormalized for easier display
    private LocalDate scheduledDate;
    private ScheduleStatus status;

    public VaccineSchedule() {}

    public VaccineSchedule(String childId, String vaccineId, String vaccineName, LocalDate scheduledDate, ScheduleStatus status) {
        this.childId = childId;
        this.vaccineId = vaccineId;
        this.vaccineName = vaccineName;
        this.scheduledDate = scheduledDate;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getChildId() { return childId; }
    public void setChildId(String childId) { this.childId = childId; }
    public String getVaccineId() { return vaccineId; }
    public void setVaccineId(String vaccineId) { this.vaccineId = vaccineId; }
    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public ScheduleStatus getStatus() { return status; }
    public void setStatus(ScheduleStatus status) { this.status = status; }
}


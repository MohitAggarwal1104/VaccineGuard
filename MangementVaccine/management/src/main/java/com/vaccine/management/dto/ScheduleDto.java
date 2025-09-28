package com.vaccine.management.dto;

public class ScheduleDto {
    private String id;
    private String childId;
    private String vaccineId;
    private String vaccineName;
    private String scheduledDate;
    private String status;

    public ScheduleDto() {}

    public ScheduleDto(String id, String childId, String vaccineId, String vaccineName, String scheduledDate, String status) {
        this.id = id;
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
    public String getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


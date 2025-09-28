package com.vaccine.management.dto;

import java.util.List;

public class ChildDto {
    private String id;
    private String name;
    private String dob;
    private String parentId;
    private String doctorId;
    private List<ScheduleDto> schedules;

    public ChildDto() {}

    public ChildDto(String id, String name, String dob, String parentId, String doctorId, List<ScheduleDto> schedules) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.parentId = parentId;
        this.doctorId = doctorId;
        this.schedules = schedules;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public List<ScheduleDto> getSchedules() { return schedules; }
    public void setSchedules(List<ScheduleDto> schedules) { this.schedules = schedules; }
}


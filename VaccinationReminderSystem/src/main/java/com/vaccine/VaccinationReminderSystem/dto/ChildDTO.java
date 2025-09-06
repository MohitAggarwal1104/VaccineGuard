package com.vaccine.VaccinationReminderSystem.dto;

import java.util.List;

public class ChildDTO {
    private Long id;
    private String name;
    private String dob;
    private List<ScheduleDTO> schedules;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public List<ScheduleDTO> getSchedules() { return schedules; }
    public void setSchedules(List<ScheduleDTO> schedules) { this.schedules = schedules; }
    
    private String parentEmail; // <-- ADD THIS LINE

	public String getParentEmail() {
		return parentEmail;
	}
	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}
}
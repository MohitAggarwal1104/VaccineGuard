package com.vaccine.management.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "children")
public class Child {
    @Id
    private String id;
    private String name;
    private LocalDate dob;
    private String parentId; // Store the User ID of the parent
    private String doctorId; // Store the User ID of the doctor who added the child

    @DBRef // This links to VaccineSchedule documents
    private List<VaccineSchedule> schedules;

    public Child() {}

    public Child(String name, LocalDate dob, String parentId, String doctorId) {
        this.name = name;
        this.dob = dob;
        this.parentId = parentId;
        this.doctorId = doctorId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public List<VaccineSchedule> getSchedules() { return schedules; }
    public void setSchedules(List<VaccineSchedule> schedules) { this.schedules = schedules; }
}


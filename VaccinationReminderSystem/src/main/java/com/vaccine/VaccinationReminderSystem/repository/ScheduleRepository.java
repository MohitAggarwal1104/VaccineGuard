package com.vaccine.VaccinationReminderSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaccine.VaccinationReminderSystem.model.Schedule;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s JOIN FETCH s.vaccine WHERE s.child.id = :childId")
    List<Schedule> findByChildIdWithVaccine(@Param("childId") Long childId);

    List<Schedule> findByChildId(Long childId);
}

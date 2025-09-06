//package com.vaccine.VaccinationReminderSystem.repository;
//
//import com.vaccine.VaccinationReminderSystem.model.Child;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface ChildRepository extends JpaRepository<Child, Long> {
//    
//    // This query fetches children, their schedules, and the vaccine for each schedule all at once.
//    @Query("SELECT DISTINCT c FROM Child c LEFT JOIN FETCH c.schedules s LEFT JOIN FETCH s.vaccine WHERE c.parentEmail = :email")
//    List<Child> findByParentEmailWithSchedulesAndVaccines(@Param("email") String email);
//
//    // You might have this method already, you can leave it.
//    List<Child> findByParentEmail(String email);
//}
package com.vaccine.VaccinationReminderSystem.repository;

import com.vaccine.VaccinationReminderSystem.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    /**
     * This is the corrected, high-performance query.
     * It fetches children and all their related schedules/vaccines in a single database trip.
     * The WHERE clause now correctly references `c.parent.email`.
     */
    @Query("SELECT DISTINCT c FROM Child c LEFT JOIN FETCH c.schedules s LEFT JOIN FETCH s.vaccine WHERE c.parent.email = :email")
    List<Child> findByParentEmail(@Param("email") String email);

}
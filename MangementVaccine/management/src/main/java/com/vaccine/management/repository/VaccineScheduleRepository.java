package com.vaccine.management.repository;

import com.vaccine.management.model.VaccineSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VaccineScheduleRepository extends MongoRepository<VaccineSchedule, String> {
    List<VaccineSchedule> findByChildId(String childId);
}


package com.vaccine.management.repository;

import com.vaccine.management.model.Child;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChildRepository extends MongoRepository<Child, String> {
    List<Child> findByParentId(String parentId);
    List<Child> findByDoctorId(String doctorId);
}


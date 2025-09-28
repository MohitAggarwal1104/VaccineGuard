package com.vaccine.management.repository;

import com.vaccine.management.model.Vaccine;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface VaccineRepository extends MongoRepository<Vaccine, String> {
    Optional<Vaccine> findByName(String name);
}


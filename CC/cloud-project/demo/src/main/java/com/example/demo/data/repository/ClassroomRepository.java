package com.example.demo.data.repository;

import com.example.demo.data.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Classroom findByCode(String code);
}

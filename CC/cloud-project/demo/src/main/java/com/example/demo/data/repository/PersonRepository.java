package com.example.demo.data.repository;

import com.example.demo.data.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
    Person findByName(String name);
}

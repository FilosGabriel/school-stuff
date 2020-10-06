package com.example.demo.data.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
    @Id
    private String id;
    private String name;
    private Map<String, List<String>> classroms;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
        classroms = new HashMap<>();
    }

    public void addPresence(String classroom, String day) {
        this.classroms.get(classroom).add(day);
    }

    public void addClassroom(String name) {
        classroms.put(name, new ArrayList<>());
    }

    public Map<String, List<String>> getClassroms() {
        return classroms;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

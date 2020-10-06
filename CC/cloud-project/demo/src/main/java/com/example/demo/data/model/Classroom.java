package com.example.demo.data.model;


import org.springframework.data.annotation.Id;

import java.util.*;

public class Classroom {
    private String name;
    private Person author;
    private String description;
    private String code;
    //    day
    private Map<String, List<Attendence>> attendencesOnDay;

    public List<Attendence> getAttendencesOnDay(String day) {
        List<Attendence> attendenceList = attendencesOnDay.get(day);
        if (attendenceList == null)
            return new ArrayList<>();
        return attendenceList;
    }

    public Classroom(String name, Person author, String description, String code) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.code = code;
        this.attendencesOnDay = new HashMap<>();
    }


    public boolean hasAttendence(String day, String userId) {
        return this.attendencesOnDay.get(day)
                .stream()
                .filter(a -> a.getPersonId().equals(userId))
                .map(Attendence::isPresent)
                .findFirst().orElse(false);
    }

    public void addAttendence(String day, Attendence attendence) {
        List<Attendence> attendenceList = this.attendencesOnDay.get(day);
        if (attendenceList != null) {
            attendenceList.add(attendence);
            return;
        }
        List<Attendence> newAttendeces = new ArrayList<>();
        newAttendeces.add(attendence);
        attendencesOnDay.put(day, newAttendeces);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


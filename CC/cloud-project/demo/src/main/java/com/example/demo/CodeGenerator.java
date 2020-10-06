package com.example.demo;

import com.example.demo.data.model.Person;

import java.util.Date;

public class CodeGenerator {
    public static String generateCode(Person person, String classroomName) {
        return String.valueOf((person.getName() + classroomName + new Date().toString()).hashCode());
    }
}

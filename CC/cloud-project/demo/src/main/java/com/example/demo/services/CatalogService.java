package com.example.demo.services;

import com.example.demo.CodeGenerator;
import com.example.demo.data.model.Attendence;
import com.example.demo.data.model.Classroom;
import com.example.demo.data.model.Person;
import com.example.demo.data.repository.ClassroomRepository;
import com.example.demo.data.repository.PersonRepository;
import com.example.demo.dto.CreateClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    public List<String> getAllPersonsPresentOnDayOnClassroom(String day, String classroomCode) {
        return repository.findByCode(classroomCode).getAttendencesOnDay(day)
                .stream()
                .map(Attendence::getPersonId)
                .map(id -> personRepository.findByName(id).getName())
                .collect(Collectors.toList());

    }

    public void addPressenceForPersonToClass(String personId, String classroomCode, String day) {
        List<String> presences = personRepository.findByName(personId).getClassroms().get(classroomCode);
        if (presences != null && presences.contains(day))
            return;
        Classroom classroom = repository.findByCode(classroomCode);
        classroom.addAttendence(day, new Attendence(personId, true));
    }

    public String createClassRoom(CreateClassroom classroomInput, Person person) {
        String code = CodeGenerator.generateCode(person, classroomInput.getName());
        Classroom classroom = new Classroom(classroomInput.getName(), person, classroomInput.getDescription(), code);
        repository.insert(classroom);
        return code;
    }

    public CatalogService(@Autowired ClassroomRepository repository, @Autowired PersonRepository personRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
    private final ClassroomRepository repository;

}

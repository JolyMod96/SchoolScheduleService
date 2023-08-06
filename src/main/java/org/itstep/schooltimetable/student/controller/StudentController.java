package org.itstep.schooltimetable.student.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.student.entity.Student;
import org.itstep.schooltimetable.student.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = {"/student/", "/student"})
public class StudentController {

    private final StudentRepository repository;

    @GetMapping
    public String index(Authentication authentication) {
        String username = ((User) authentication.getPrincipal()).getUsername();
        Student student = repository.findByUserByUsername(username);

        return "student/index";
    }
}

package org.itstep.schooltimetable.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/student/", "/student"})
public class StudentController {

    @GetMapping
    public String index() {
        return "student/index";
    }
}

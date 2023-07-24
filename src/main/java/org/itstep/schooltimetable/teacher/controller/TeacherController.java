package org.itstep.schooltimetable.teacher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/teacher/", "/teacher"})
public class TeacherController {

    @GetMapping
    public String index() {
        return "/teacher/index";
    }
}

package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/admin/student/", "/admin/student"})
@RequiredArgsConstructor
public class AdminStudentController {
    private final StudentService studentService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("students", studentService.findAllStudents());
        model.addAttribute("command", new CreateStudentCommand());
        return "admin/student/index";
    }

    @PostMapping
    public String create(@Validated CreateStudentCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "admin/student/index";
        }
        studentService.save(command);
        return "redirect:/admin/student/";
    }

}

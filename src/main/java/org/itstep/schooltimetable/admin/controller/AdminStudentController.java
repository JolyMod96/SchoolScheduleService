package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.command.EditStudentCommand;
import org.itstep.schooltimetable.admin.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminStudentController {
    private final StudentService studentService;

    @GetMapping(path = {"/admin/student/", "/admin/student"})
    public String index(Model model) {
        model.addAttribute("students", studentService.findAllStudents());
        return "admin/student/index";
    }

    @GetMapping(path = {"/admin/student/create/", "/admin/student/create"})
    public String studentCreate(Model model) {
        //model.addAttribute("students", studentService.findAllStudents());
        model.addAttribute("command", new CreateStudentCommand());
        return "/admin/student/create";
    }

    @PostMapping(path = {"/admin/student/create/", "/admin/student/create"})
    public String create(@Validated CreateStudentCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "/admin/student/create";
        }
        studentService.save(command);
        return "redirect:/admin/student/";
    }

    @GetMapping(path = {"/admin/student/{id}/edit", "/admin/student/{id}/edit/"})
    public String teacherEdit(@PathVariable(value = "id") long id, Model model) {
        var student =  studentService.findById(id).orElseThrow();
        var command = new EditStudentCommand(student.getFirstName(), student.getLastName());
        model.addAttribute("command", command);
        return "admin/student/edit";
    }

    @PostMapping(path = {"/admin/student/{id}/edit", "/admin/student/{id}/edit/"})
    public String update(@PathVariable(value = "id") long id, @Validated EditStudentCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "admin/student/edit";
        }
        studentService.edit(id, command);
        return "redirect:/admin/student/";
    }

    @GetMapping(path = {"/admin/student/{id}/delete", "/admin/student/{id}/delete/"})
    public String delete(@PathVariable(value = "id") long id) {
        var student = studentService.findById(id).orElseThrow();
        studentService.delete(student);
        return "redirect:/admin/student/";
    }

}

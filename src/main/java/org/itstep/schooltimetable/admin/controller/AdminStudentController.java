package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.command.EditStudentCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.admin.service.GroupService;
import org.itstep.schooltimetable.admin.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
    private final GroupService groupService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/student/", "/admin/student"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("students", studentService.findAllStudents());
        return "admin/student/index";
    }

    @GetMapping(path = {"/admin/student/create/", "/admin/student/create"})
    public String studentCreate(Authentication authentication, Model model) {
        //model.addAttribute("students", studentService.findAllStudents());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("command", new CreateStudentCommand());
        model.addAttribute("groups", groupService.findAllGroups());
        return "/admin/student/create";
    }

    @PostMapping(path = {"/admin/student/create/", "/admin/student/create"})
    public String create(@Validated CreateStudentCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("username", username);
            model.addAttribute("command", command);
            return "/admin/student/create";
        }
        studentService.save(command);
        return "redirect:/admin/student/";
    }

    @GetMapping(path = {"/admin/student/{id}/edit", "/admin/student/{id}/edit/"})
    public String studentEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var student = studentService.findById(id).orElseThrow();
        var command = new EditStudentCommand(student.getFirstName(), student.getLastName(), student.getGroup() != null ? student.getGroup().getId() : -1);
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("command", command);
        model.addAttribute("groups", groupService.findAllGroups());
        return "admin/student/edit";
    }

    @PostMapping(path = {"/admin/student/{id}/edit", "/admin/student/{id}/edit/"})
    public String update(@PathVariable(value = "id") long id, @Validated EditStudentCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("username", username);
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

package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateTeacherCommand;
import org.itstep.schooltimetable.admin.command.EditTeacherCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.admin.service.SubjectService;
import org.itstep.schooltimetable.admin.service.TeacherService;
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
public class AdminTeacherController {
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/teacher/", "/admin/teacher"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "admin/teacher/index";
    }

    @GetMapping(path = {"/admin/teacher/create/", "/admin/teacher/create"})
    public String teacherCreate(Authentication authentication, Model model) {
       // model.addAttribute("teachers", teacherService.findAllTeachers());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("command", new CreateTeacherCommand());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "admin/teacher/create";
    }

    @PostMapping(path = {"/admin/teacher/create/", "/admin/teacher/create"})
    public String create(@Validated CreateTeacherCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("username", username);
            model.addAttribute("command", command);
            return "admin/teacher/create";
        }
        teacherService.save(command);
        return "redirect:/admin/teacher/";
    }

    @GetMapping(path = {"/admin/teacher/{id}/edit", "/admin/teacher/{id}/edit/"})
    public String teacherEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var teacher =  teacherService.findById(id).orElseThrow();
        var command = new EditTeacherCommand(teacher.getFirstName(), teacher.getLastName(), teacher.getSubjectsId());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("command", command);
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "admin/teacher/edit";
    }

    @PostMapping(path = {"/admin/teacher/{id}/edit", "/admin/teacher/{id}/edit/"})
    public String update(@PathVariable(value = "id") long id, @Validated EditTeacherCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("username", username);
            model.addAttribute("command", command);
            return "admin/teacher/edit";
        }
        teacherService.edit(id, command);
        return "redirect:/admin/teacher/";
    }

    @GetMapping(path = {"/admin/teacher/{id}/delete", "/admin/teacher/{id}/delete/"})
    public String delete(@PathVariable(value = "id") long id) {
        var teacher = teacherService.findById(id).orElseThrow();
        teacherService.delete(teacher);
        return "redirect:/admin/teacher/";
    }

}

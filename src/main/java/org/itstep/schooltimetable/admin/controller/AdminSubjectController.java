package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateSubjectCommand;
import org.itstep.schooltimetable.admin.command.EditGroupCommand;
import org.itstep.schooltimetable.admin.command.EditSubjectCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.admin.service.SubjectService;
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
public class AdminSubjectController {
    private final SubjectService subjectService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/subject/", "/admin/subject"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "admin/subject/index";
    }

    @GetMapping(path = {"/admin/subject/create/", "/admin/subject/create"})
    public String subjectCreate(Authentication authentication, Model model) {
        //model.addAttribute("subjects", subjectService.findAllSubjects());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("command", new CreateSubjectCommand());
        return "/admin/subject/create";
    }

    @PostMapping(path = {"/admin/subject/create/", "/admin/subject/create"})
    public String create(@Validated CreateSubjectCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/subject/create";
        }
        subjectService.save(command.getName());
        return "redirect:/admin/subject";
    }

    @GetMapping(path = {"/admin/subject/{id}/edit/", "/admin/subject/{id}/edit"})
    public String subjectEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var subject = subjectService.findById(id).orElseThrow();
        var command = new EditSubjectCommand(subject.getName());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("command", command);
        return "/admin/subject/edit";
    }

    @PostMapping(path = {"/admin/subject/{id}/edit/", "/admin/subject/{id}/edit"})
    public String update(@PathVariable(value = "id") long id, @Validated EditSubjectCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/subject/edit";
        }
        subjectService.edit(id, command.getName());
        return "redirect:/admin/subject/";
    }

    @GetMapping(path = {"/admin/subject/{id}/delete/", "/admin/subject/{id}/delete"})
    public String delete(@PathVariable(value = "id") long id) {
        var subject = subjectService.findById(id).orElseThrow();
        subjectService.delete(subject);
        return "redirect:/admin/subject/";
    }

}

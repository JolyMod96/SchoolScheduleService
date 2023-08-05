package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateSubjectCommand;
import org.itstep.schooltimetable.admin.command.EditGroupCommand;
import org.itstep.schooltimetable.admin.command.EditSubjectCommand;
import org.itstep.schooltimetable.admin.service.SubjectService;
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

    @GetMapping(path = {"/admin/subject/", "/admin/subject"})
    public String index(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "admin/subject/index";
    }

    @GetMapping(path = {"/admin/subject/create/", "/admin/subject/create"})
    public String subjectCreate(Model model) {
        //model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("command", new CreateSubjectCommand());
        return "/admin/subject/create";
    }

    @PostMapping(path = {"/admin/subject/create/", "/admin/subject/create"})
    public String create(@Validated CreateSubjectCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "/admin/subject/create";
        }
        subjectService.save(command.getName());
        return "redirect:/admin/subject";
    }

    @GetMapping(path = {"/admin/subject/{id}/edit/", "/admin/subject/{id}/edit"})
    public String subjectEdit(@PathVariable(value = "id") long id, Model model) {
        var subject = subjectService.findById(id).orElseThrow();
        var command = new EditSubjectCommand(subject.getName());
        model.addAttribute("command", command);
        return "/admin/subject/edit";
    }

    @PostMapping(path = {"/admin/subject/{id}/edit/", "/admin/subject/{id}/edit"})
    public String update(@PathVariable(value = "id") long id, @Validated EditSubjectCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
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

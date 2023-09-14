package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.EditTimetableCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.admin.service.TimetableService;
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
public class AdminTimetableController {
    private final TimetableService timetableService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/timetable/", "/admin/timetable"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("timetables", timetableService.findAllTimetables());
        return "admin/timetable/index";
    }

    @GetMapping(path = {"/admin/timetable/{id}/edit/", "/admin/timetable/{id}/edit"})
    public String timetableEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var timetable = timetableService.findById(id).orElseThrow();
        var command = new EditTimetableCommand(timetable.getTimeStart().toString(), timetable.getTimeEnd().toString());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("username", username);
        model.addAttribute("timetableName", timetable.getName());
        model.addAttribute("command", command);
        return "admin/timetable/edit";
    }

    @PostMapping(path = {"/admin/timetable/{id}/edit/", "/admin/timetable/{id}/edit"})
    public String update(@PathVariable(value = "id") long id, @Validated EditTimetableCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("username", username);
            model.addAttribute("command", command);
            return "admin/timetable/edit";
        }
        timetableService.edit(id, command);
        return "redirect:/admin/timetable";
    }
}

package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateGroupCommand;
import org.itstep.schooltimetable.admin.command.EditGroupCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.admin.service.GroupService;
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
public class AdminGroupController {
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/group/", "/admin/group"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("groups", groupService.findAllGroups());
        return "admin/group/index";
    }

    @GetMapping(path = {"/admin/group/create/", "/admin/group/create"})
    public String groupCreate(Authentication authentication, Model model) {
        //model.addAttribute("groups", groupService.findAllGroups());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("command", new CreateGroupCommand());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "/admin/group/create";
    }

    @PostMapping(path = {"/admin/group/create/", "/admin/group/create"})
    public String create(@Validated CreateGroupCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/group/create";
        }
        groupService.save(command);
        return "redirect:/admin/group/";
    }

    @GetMapping(path = {"/admin/group/{id}/edit/", "/admin/group/{id}/edit"})
    public String groupEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        var group = groupService.findById(id).orElseThrow();
        var command = new EditGroupCommand(group.getName(), group.getSubjectsId());
        model.addAttribute("command", command);
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "/admin/group/edit";
    }

    @PostMapping(path = {"/admin/group/{id}/edit/", "/admin/group/{id}/edit"})
    public String update(@PathVariable(value = "id") long id, @Validated EditGroupCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/group/edit";
        }
        groupService.edit(id, command);
        return "redirect:/admin/group/";
    }

    @GetMapping(path = {"/admin/group/{id}/delete/", "/admin/group/{id}/delete"})
    public String delete(@PathVariable(value = "id") long id) {
        var group = groupService.findById(id).orElseThrow();
        groupService.delete(group);
        return "redirect:/admin/group/";
    }

}

package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateGroupCommand;
import org.itstep.schooltimetable.admin.command.EditGroupCommand;
import org.itstep.schooltimetable.admin.service.GroupService;
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

    @GetMapping(path = {"/admin/group/", "/admin/group"})
    public String index(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "admin/group/index";
    }

    @GetMapping(path = {"/admin/group/create/", "/admin/group/create"})
    public String groupCreate(Model model) {
        //model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("command", new CreateGroupCommand());
        return "/admin/group/create";
    }

    @PostMapping(path = {"/admin/group/create/", "/admin/group/create"})
    public String create(@Validated CreateGroupCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "/admin/group/create";
        }
        groupService.save(command.getName());
        return "redirect:/admin/group/";
    }

    @GetMapping(path = {"/admin/group/{id}/edit", "/admin/group/{id}/edit/"})
    public String groupEdit(@PathVariable(value = "id") long id, Model model) {
        var group =  groupService.findById(id).orElseThrow();
        var command = new EditGroupCommand(group.getName());
        model.addAttribute("command", command);
        return "admin/group/edit";
    }

    @PostMapping(path = {"/admin/group/{id}/edit", "/admin/group/{id}/edit/"})
    public String update(@PathVariable(value = "id") long id, @Validated EditGroupCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "admin/group/edit";
        }
        groupService.edit(id, command.getName());
        return "redirect:/admin/group/";
    }

    @GetMapping(path = {"/admin/group/{id}/delete", "/admin/group/{id}/delete/"})
    public String delete(@PathVariable(value = "id") long id) {
        var group = groupService.findById(id).orElseThrow();
        groupService.delete(group);
        return "redirect:/admin/group/";
    }

}

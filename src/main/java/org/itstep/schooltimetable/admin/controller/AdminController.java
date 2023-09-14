package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateAdminCommand;
import org.itstep.schooltimetable.admin.service.AdminService;
import org.itstep.schooltimetable.security.entity.CustomRole;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping(path = {"/admin/", "/admin"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("admins", adminService.findAllAdmins());
        return "admin/index";
    }

    @GetMapping(path = {"/admin/table/", "/admin/table"})
    public String table(Model model) {
        model.addAttribute("admins", adminService.findAllAdmins());
        return "admin/table";
    }

    @GetMapping(path = {"/admin/create/", "/admin/create"})
    public String adminCreate(Model model) {
        //model.addAttribute("admins", adminService.findAllAdmins());
        model.addAttribute("command", new CreateAdminCommand());
        return "admin/create";
    }

    @PostMapping(path = {"/admin/create/", "/admin/create"})
    public String create(@Validated CreateAdminCommand command, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("command", command);
            return "admin/create";
        }
        adminService.save(command);
        return "redirect:/admin/table/";
    }

    @GetMapping(path = {"/admin/{id}/delete/", "/admin/{id}/delete"})
    public String delete(@PathVariable(value = "id") long id) {
        var admin = adminService.findById(id).orElseThrow();
        adminService.delete(admin);
        return "redirect:/admin/table/";
    }

}

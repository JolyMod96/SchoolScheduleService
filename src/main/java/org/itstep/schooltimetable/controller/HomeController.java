package org.itstep.schooltimetable.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.security.command.ChangePasswordCommand;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(path = "/")
    public String index() {
        return "/index";
    }

    @GetMapping(path = { "/home/", "/home" })
    public String home(Authentication authentication, Model model) {
        var user = customUserRepository.findByUsername(((User) authentication.getPrincipal()).getUsername()).orElseThrow();
        String firstName = null;
        String lastName = null;
        if (user.getStudent() != null){
            firstName = user.getStudent().getFirstName();
            lastName = user.getStudent().getLastName();
        }
        else if (user.getTeacher() != null) {
            firstName = user.getTeacher().getFirstName();
            lastName = user.getTeacher().getLastName();
        }
        model.addAttribute("user", user);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        return "/home/index";
    }

    @GetMapping(path = { "/change-password/", "/change-password" })
    public String changePassword(Model model) {
        model.addAttribute("command", new ChangePasswordCommand());
        return "/change-password/index";
    }

    @PostMapping(path = { "/change-password/", "/change-password" })
    public String change(@Validated ChangePasswordCommand command, BindingResult bindingResult, Model model, Authentication authentication) {
        // TODO: 31.08.2023 fix password checking problem
        var user = customUserRepository.findByUsername(((User) authentication.getPrincipal()).getUsername()).orElseThrow();
        if (bindingResult.hasErrors() || user.getPassword() != passwordEncoder.encode(command.getOldPassword())) {
            model.addAttribute("errorMessage", "Incorrect password");
            model.addAttribute("command", command);
            return "/change-password/index";
        }
        // TODO: 31.08.2023 implement password changing
        return "redirect:/home/";
    }

}

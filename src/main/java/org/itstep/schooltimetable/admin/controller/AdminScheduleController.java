package org.itstep.schooltimetable.admin.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateScheduleCommand;
import org.itstep.schooltimetable.admin.command.EditScheduleCommand;
import org.itstep.schooltimetable.admin.command.EditTimetableCommand;
import org.itstep.schooltimetable.admin.service.*;
import org.itstep.schooltimetable.schedule.repository.DayOfWeekRepository;
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
public class AdminScheduleController {
    private final ScheduleService scheduleService;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TimetableService timetableService;
    private final SubjectService subjectService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final AdminService adminService;

    @GetMapping(path = {"/admin/schedule/", "/admin/schedule"})
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("schedules", scheduleService.findAllSchedules());
        return "admin/schedule/index";
    }

    @GetMapping(path = { "/admin/schedule/create/", "/admin/schedule/create" })
    public String scheduleCreate(Authentication authentication, Model model) {
        //model.addAttribute("schedules", scheduleService.findAllSchedules());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("command", new CreateScheduleCommand());
        model.addAttribute("daysOfWeek", dayOfWeekRepository.findAll());
        model.addAttribute("timetables", timetableService.findAllTimetables());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "/admin/schedule/create";
    }

    @PostMapping(path = { "/admin/schedule/create/", "/admin/schedule/create" })
    public String create(@Validated CreateScheduleCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/schedule/create";
        }
        scheduleService.save(command);
        return "redirect:/admin/schedule";
    }

    @GetMapping(path = { "/admin/schedule/{id}/edit/", "/admin/schedule/{id}/edit" })
    public String scheduleEdit(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        var schedule = scheduleService.findById(id).orElseThrow();
        var command = new EditScheduleCommand(schedule.getDateStart(),
                schedule.getDateEnd(), schedule.getWeeksRepeat(),
                schedule.getDaysOfWeekId(), schedule.getTimetable().getId(),
                schedule.getSubject().getId(), schedule.getGroup().getId(), schedule.getIsSubstituteTeacher(), schedule.getTeacher().getId());
        var username = ((User) authentication.getPrincipal()).getUsername();
        var admin = adminService.findByUsername(username);
        model.addAttribute("isAdminCreator", admin.isAdminCreator());
        model.addAttribute("command", command);
        model.addAttribute("daysOfWeek", dayOfWeekRepository.findAll());
        model.addAttribute("timetables", timetableService.findAllTimetables());
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("teachers", teacherService.findAllTeachers());
        return "/admin/schedule/edit";
    }

    @PostMapping(path = { "/admin/schedule/{id}/edit/", "/admin/schedule/{id}/edit" })
    public String update(@PathVariable(value = "id") long id, @Validated EditScheduleCommand command, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var username = ((User) authentication.getPrincipal()).getUsername();
            var admin = adminService.findByUsername(username);
            model.addAttribute("isAdminCreator", admin.isAdminCreator());
            model.addAttribute("command", command);
            return "/admin/schedule/edit";
        }
        scheduleService.edit(id, command);
        return "redirect:/admin/schedule";
    }

    @GetMapping(path = { "/admin/schedule/{id}/delete/", "/admin/schedule/{id}/delete" })
    public String delete(@PathVariable(value = "id") long id) {
        var schedule = scheduleService.findById(id).orElseThrow();
        scheduleService.delete(schedule);
        return "redirect:/admin/schedule";
    }

}

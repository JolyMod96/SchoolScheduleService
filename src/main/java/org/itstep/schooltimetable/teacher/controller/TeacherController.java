package org.itstep.schooltimetable.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.schedule.entity.DayOfWeek;
import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.itstep.schooltimetable.schedule.entity.Timetable;
import org.itstep.schooltimetable.schedule.repository.DayOfWeekRepository;
import org.itstep.schooltimetable.schedule.repository.ScheduleRepository;
import org.itstep.schooltimetable.schedule.repository.TimetableRepository;
import org.itstep.schooltimetable.teacher.repository.TeacherRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TimetableRepository timetableRepository;

    @GetMapping(path = { "/teacher/", "/teacher" })
    public String index(Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var teacher = teacherRepository.findByUserByUsername(username);
        model.addAttribute("teacher", teacher);
        return "/teacher/index";
    }

    @GetMapping(path = { "/teacher/schedule/", "/teacher/schedule" })
    public String getScheduleByThisWeek() {
        var mondayDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        return String.format("redirect:/teacher/schedule/%d/%d/%d", mondayDate.getDayOfMonth(), mondayDate.getMonth().getValue(), mondayDate.getYear());
    }

    @GetMapping(path = {"/teacher/schedule/{dayOfMonth}/{month}/{year}/", "/teacher/schedule/{dayOfMonth}/{month}/{year}"})
    public String getSchedule(@PathVariable(value = "dayOfMonth") int dayOfMonth, @PathVariable(value = "month") int month, @PathVariable(value = "year") int year, Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var teacher = teacherRepository.findByUserByUsername(username);
        var schedules = scheduleRepository.findAllByTeacherId(teacher.getId());
        var selectedWeekDate = LocalDate.of(year, month, dayOfMonth).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        var currentWeekSchedules = new ArrayList<>(schedules.stream()
                .filter(schedule -> selectedWeekDate.compareTo(schedule.getDateStart()) > -1)
                .sorted(Comparator.comparingLong(schedule -> schedule.getTimetable().getId())).toList());

        var daysOfWeek = dayOfWeekRepository.findAll().stream()
                .sorted(Comparator.comparingLong(DayOfWeek::getId)).toList();

        var weekDaysTimetablesSchedules = new HashMap<DayOfWeek, Map<Timetable, List<Schedule>>>();

        for (var schedule : currentWeekSchedules) {
            for (var dayOfWeek : daysOfWeek) {
                if (schedule.getDaysOfWeek().contains(dayOfWeek) &&
                        selectedWeekDate.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.values()[dayOfWeek.getId().intValue() - 1])).compareTo(schedule.getDateEnd()) < 1) {
                    if (!weekDaysTimetablesSchedules.containsKey(dayOfWeek)) {
                        weekDaysTimetablesSchedules.put(dayOfWeek, new HashMap<>());
                    }
                    if (!weekDaysTimetablesSchedules.get(dayOfWeek).containsKey(schedule.getTimetable())) {
                        weekDaysTimetablesSchedules.get(dayOfWeek).put(schedule.getTimetable(), new ArrayList<>());
                    }
                    weekDaysTimetablesSchedules.get(dayOfWeek).get(schedule.getTimetable()).add(schedule);
                }
            }
        }

        var previousWeek = selectedWeekDate.with(TemporalAdjusters.previous(java.time.DayOfWeek.MONDAY));
        var nextWeek = selectedWeekDate.with(TemporalAdjusters.next(java.time.DayOfWeek.MONDAY));
        model.addAttribute("currentWeek", selectedWeekDate);
        model.addAttribute("previousWeek", previousWeek);
        model.addAttribute("nextWeek", nextWeek);
        model.addAttribute("previousWeekUrl", "/teacher/schedule/%d/%d/%d".formatted(previousWeek.getDayOfMonth(), previousWeek.getMonth().getValue(), previousWeek.getYear()));
        model.addAttribute("nextWeekUrl", "/teacher/schedule/%d/%d/%d".formatted(nextWeek.getDayOfMonth(), nextWeek.getMonth().getValue(), nextWeek.getYear()));

        model.addAttribute("teacher", teacher);
        model.addAttribute("weekDaysTimetablesSchedules", weekDaysTimetablesSchedules);
        model.addAttribute("daysOfWeek", daysOfWeek);
        var timetables = timetableRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Timetable::getId)).toList();
        model.addAttribute("timetables", timetables);
        return "teacher/schedule/index";
    }

    @GetMapping(path = { "/teacher/schedule/{id}/details/" , "/teacher/schedule/{id}/details" })
    public String getScheduleDetails(@PathVariable(name = "id") long id, Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var teacher = teacherRepository.findByUserByUsername(username);
        var schedule = scheduleRepository.findById(id).orElseThrow();
        if (teacher.getId() != schedule.getTeacher().getId()) {
            return "redirect:/teacher/schedule";
        }

        model.addAttribute("teacher", teacher);
        model.addAttribute("schedule", schedule);

        return "teacher/schedule/details";
    }

}

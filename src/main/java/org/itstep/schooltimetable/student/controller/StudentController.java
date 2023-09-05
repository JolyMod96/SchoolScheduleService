package org.itstep.schooltimetable.student.controller;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.schedule.entity.DayOfWeek;
import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.itstep.schooltimetable.schedule.entity.Timetable;
import org.itstep.schooltimetable.schedule.repository.DayOfWeekRepository;
import org.itstep.schooltimetable.schedule.repository.ScheduleRepository;
import org.itstep.schooltimetable.schedule.repository.TimetableRepository;
import org.itstep.schooltimetable.student.repository.StudentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TimetableRepository timetableRepository;

    @GetMapping(path = {"/student/", "/student"})
    public String index(Authentication authentication, Model model) {
//        var username = ((User) authentication.getPrincipal()).getUsername();
//        var student = studentRepository.findByUserByUsername(username);
//        model.addAttribute("group", student.getGroup());

        var username = ((User) authentication.getPrincipal()).getUsername();
        var student = studentRepository.findByUserByUsername(username);
        model.addAttribute("student", student);

        return "student/index";
    }

    @GetMapping(path = { "/student/schedule/", "/student/schedule" })
    public String getScheduleByThisWeek() {
        var mondayDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        return String.format("redirect:/student/schedule/%d/%d/%d", mondayDate.getDayOfMonth(), mondayDate.getMonth().getValue(), mondayDate.getYear());
    }

    @GetMapping(path = {"/student/schedule/{dayOfMonth}/{month}/{year}/", "/student/schedule/{dayOfMonth}/{month}/{year}"})
    public String getSchedule(@PathVariable(value = "dayOfMonth") int dayOfMonth, @PathVariable(value = "month") int month, @PathVariable(value = "year") int year, Authentication authentication, Model model) {
        var username = ((User) authentication.getPrincipal()).getUsername();
        var student = studentRepository.findByUserByUsername(username);
        var schedules = scheduleRepository.findAllByGroupId(student.getGroup().getId());
        var selectedWeekDate = LocalDate.of(year, month, dayOfMonth);
        var currentWeekSchedules = new ArrayList<>(schedules.stream()
                .filter(schedule -> selectedWeekDate.compareTo(schedule.getDateStart()) > -1 &&
                        selectedWeekDate.plusDays(6).compareTo(schedule.getDateEnd()) < 1)
                .sorted(Comparator.comparingLong(schedule -> schedule.getTimetable().getId())).toList());

        var daysOfWeek = dayOfWeekRepository.findAll().stream()
                .sorted(Comparator.comparingLong(DayOfWeek::getId)).toList();

        var weekDaysTimetablesSchedules = new HashMap<DayOfWeek, Map<Timetable, List<Schedule>>>();

        for (var schedule : currentWeekSchedules) {
            for (var dayOfWeek : daysOfWeek) {
                if (schedule.getDaysOfWeek().contains(dayOfWeek)) {
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

        model.addAttribute("student", student);
        model.addAttribute("weekDaysTimetablesSchedules", weekDaysTimetablesSchedules);
        model.addAttribute("daysOfWeek", daysOfWeek);
        var timetables = timetableRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Timetable::getId)).toList();
        model.addAttribute("timetables", timetables);
        return "student/schedule/index";
    }

}

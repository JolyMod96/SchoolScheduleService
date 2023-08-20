package org.itstep.schooltimetable.init;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.*;
import org.itstep.schooltimetable.admin.service.*;
import org.itstep.schooltimetable.schedule.entity.DayOfWeek;
import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.itstep.schooltimetable.schedule.entity.Timetable;
import org.itstep.schooltimetable.schedule.repository.DayOfWeekRepository;
import org.itstep.schooltimetable.schedule.repository.TimetableRepository;
import org.itstep.schooltimetable.security.entity.CustomRole;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.security.repository.CustomUserRepository;
import org.itstep.schooltimetable.subject.entity.Subject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {
    private final CustomRoleRepository roleRepository;
    private final StudentService studentService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final AdminService adminService;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TimetableRepository timetableRepository;
    private final ScheduleService scheduleService;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdminCreator = roleRepository.save(new CustomRole("ROLE_ADMIN_CREATOR"));
        var roleAdmin = roleRepository.save(new CustomRole("ROLE_ADMIN"));
        var roleStudent = roleRepository.save(new CustomRole("ROLE_STUDENT"));
        var roleTeacher = roleRepository.save(new CustomRole("ROLE_TEACHER"));

        var dayOfWeekMonday = dayOfWeekRepository.save(new DayOfWeek("Monday"));
        var dayOfWeekTuesday = dayOfWeekRepository.save(new DayOfWeek("Tuesday"));
        var dayOfWeekWednesday = dayOfWeekRepository.save(new DayOfWeek("Wednesday"));
        var dayOfWeekThursday = dayOfWeekRepository.save(new DayOfWeek("Thursday"));
        var dayOfWeekFriday = dayOfWeekRepository.save(new DayOfWeek("Friday"));
        var dayOfWeekSaturday = dayOfWeekRepository.save(new DayOfWeek("Saturday"));
        var dayOfWeekSunday = dayOfWeekRepository.save(new DayOfWeek("Sunday"));

        var firstLesson = timetableRepository.save(new Timetable(Duration.parse("PT8H30M"), Duration.parse("PT9H15M"), "1st Lesson"));
        var secondLesson = timetableRepository.save(new Timetable(Duration.parse("PT9H25M"), Duration.parse("PT10H10M"), "2nd Lesson"));
        var thirdLesson = timetableRepository.save(new Timetable(Duration.parse("PT10H30M"), Duration.parse("PT11H15M"), "3rd Lesson"));
        var fourthLesson = timetableRepository.save(new Timetable(Duration.parse("PT11H30M"), Duration.parse("PT12H15M"), "4th Lesson"));
        var fifthLesson = timetableRepository.save(new Timetable(Duration.parse("PT12H30M"), Duration.parse("PT13H15M"), "5th Lesson"));
        var sixthLesson = timetableRepository.save(new Timetable(Duration.parse("PT13H25M"), Duration.parse("PT14H10M"), "6th Lesson"));
        var seventhLesson = timetableRepository.save(new Timetable(Duration.parse("PT14H20M"), Duration.parse("PT15H5M"), "7th Lesson"));

        var subject1 = subjectService.save("subject 1");
        var subject2 = subjectService.save("subject 2");
        var subject3 = subjectService.save("subject 3");
        var subject4 = subjectService.save("subject 4");
        var subject5 = subjectService.save("subject 5");
        var subject6 = subjectService.save("subject 6");
        var subject7 = subjectService.save("subject 7");
        var subject8 = subjectService.save("subject 8");
        var subject9 = subjectService.save("subject 9");
        var subject10 = subjectService.save("subject 10");
        var subject11 = subjectService.save("subject 11");
        var subject12 = subjectService.save("subject 12");
        var subject13 = subjectService.save("subject 13");
        var subject14 = subjectService.save("subject 14");
        var subject15 = subjectService.save("subject 15");

        var allSubjectsId = List.of(subject1.getId(), subject2.getId(), subject3.getId(), subject4.getId(), subject5.getId(), subject6.getId(), subject7.getId(),
                subject8.getId(), subject9.getId(), subject10.getId(), subject11.getId(), subject12.getId(), subject13.getId(), subject14.getId(), subject15.getId());

        var group1 = groupService.save(new CreateGroupCommand("group1", allSubjectsId));
        var group2 = groupService.save(new CreateGroupCommand("group2", allSubjectsId));
        var group3 = groupService.save(new CreateGroupCommand("group3", allSubjectsId));

        var adminCreator = adminService.save(new CreateAdminCommand("adminCreator", "adminCreator", true));
        var admin = adminService.save(new CreateAdminCommand("admin", "admin", false));

        var student1 = studentService.save(new CreateStudentCommand("student1", "student1", "student1", "student1", group1.getId()));
        var student2 = studentService.save(new CreateStudentCommand("student2", "student2", "student2", "student2", group2.getId()));
        var student3 = studentService.save(new CreateStudentCommand("student3", "student3", "student3", "student3", group3.getId()));

        var teacher1 = teacherService.save(new CreateTeacherCommand("teacher1", "teacher1", "teacher1", "teacher1", new ArrayList<>()));
        var teacher2 = teacherService.save(new CreateTeacherCommand("teacher2", "teacher2", "teacher2", "teacher2", new ArrayList<>()));
        var teacher3 = teacherService.save(new CreateTeacherCommand("teacher3", "teacher3", "teacher3", "teacher3", new ArrayList<>()));

        scheduleService.save(new CreateScheduleCommand(LocalDate.of(2023, 8, 14), LocalDate.of(2024, 8, 14), 1, List.of(dayOfWeekMonday.getId(), dayOfWeekFriday.getId()), firstLesson.getId(), group1.getId(), subject1.getId(), false, teacher1.getId()));
    }
}

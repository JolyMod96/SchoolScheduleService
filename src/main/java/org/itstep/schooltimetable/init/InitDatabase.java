package org.itstep.schooltimetable.init;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateGroupCommand;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.command.CreateTeacherCommand;
import org.itstep.schooltimetable.admin.service.GroupService;
import org.itstep.schooltimetable.admin.service.StudentService;
import org.itstep.schooltimetable.admin.service.SubjectService;
import org.itstep.schooltimetable.admin.service.TeacherService;
import org.itstep.schooltimetable.security.entity.CustomRole;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.security.repository.CustomUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {
    private final CustomRoleRepository roleRepository;
    private final CustomUserRepository userRepository;
    private final StudentService studentService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.save(new CustomRole("ROLE_ADMIN"));
        var roleStudent = roleRepository.save(new CustomRole("ROLE_STUDENT"));
        var roleTeacher = roleRepository.save(new CustomRole("ROLE_TEACHER"));
        var admin = new CustomUser("admin", passwordEncoder.encode("admin"));
        admin.addRole(roleAdmin, roleStudent, roleTeacher);
        userRepository.save(admin);
        var student = new CustomUser("student", passwordEncoder.encode("student"));
        student.addRole(roleStudent);
        userRepository.save(student);

        studentService.save(new CreateStudentCommand("testStudent", "testStudent", "testStudent", "testStudent", null));
        groupService.save(new CreateGroupCommand("test group", new ArrayList<>()));

        subjectService.save("test subject 1");
        subjectService.save("test subject 2");
        subjectService.save("test subject 3");
        teacherService.save(new CreateTeacherCommand("testTeacher", "testTeacher", "testTeacher", "testTeacher", new ArrayList<>()));
    }
}

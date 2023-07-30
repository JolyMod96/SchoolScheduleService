package org.itstep.schooltimetable.init;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.service.GroupService;
import org.itstep.schooltimetable.admin.service.StudentService;
import org.itstep.schooltimetable.security.entity.CustomRole;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.security.repository.CustomUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {
    private final CustomRoleRepository roleRepository;
    private final CustomUserRepository userRepository;
    private final StudentService studentService;
    private final GroupService groupService;
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

        studentService.save(new CreateStudentCommand("test", "test", "test", "test", null));
        groupService.save("test group");
    }
}

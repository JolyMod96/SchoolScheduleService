package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.command.EditStudentCommand;
import org.itstep.schooltimetable.admin.command.EditTeacherCommand;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.student.entity.Student;
import org.itstep.schooltimetable.student.repository.StudentRepository;
import org.itstep.schooltimetable.teacher.entity.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomRoleRepository customRoleRepository;

    @Transactional
    public Student save(CreateStudentCommand command) {
        var user = new CustomUser(command.getLogin(), passwordEncoder.encode(command.getPassword()));
        var studentRole = customRoleRepository.findByAuthorityIsLike("%STUDENT%");
        user.addRole(studentRole);
        var student = new Student(command.getFirstName(), command.getLastName());
        student.setUser(user);
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(long id) {
        return studentRepository.findById(id);
    }

    public void edit(long id, EditStudentCommand command) {
        var student = studentRepository.findById(id).orElseThrow();
        student.setFirstName(command.getFirstName());
        student.setLastName(command.getLastName());
        studentRepository.save(student);
    }

    @Transactional
    public void delete(Student student) {
        studentRepository.delete(student);
    }
}

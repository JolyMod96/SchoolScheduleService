package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateStudentCommand;
import org.itstep.schooltimetable.admin.command.EditStudentCommand;
import org.itstep.schooltimetable.admin.command.EditTeacherCommand;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.group.repository.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomRoleRepository customRoleRepository;

    @Transactional
    public Student save(CreateStudentCommand command) {
        var user = new CustomUser(command.getLogin(), passwordEncoder.encode(command.getPassword()));
        var studentRole = customRoleRepository.findByAuthorityIsLike("%STUDENT%");
        user.addRole(studentRole);
        var student = new Student(command.getFirstName(), command.getLastName());
        student.setUser(user);
        if (command.getGroupId() != null && command.getGroupId() > 0) {
            groupRepository.findById(command.getGroupId()).ifPresent(student::setGroup);
        }
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

    public void addToGroup(long id, Group group) {
        var student = studentRepository.findById(id).orElseThrow();
        if (student.getGroup() != null) {
            student.removeFromGroup();
        }
        student.setGroup(group);
    }

    public void removeFromGroup(Student student) {
        if (student.getGroup() != null) {
            student.removeFromGroup();
        }
        studentRepository.save(student);
    }

    @Transactional
    public void addToGroup(long id, Long groupId) {
        Optional<Student> student = studentRepository.findById(id);
        Optional<Group> group = groupRepository.findById(groupId);
        if (student.isPresent() && group.isPresent()) {
            student.get().setGroup(group.get());
            studentRepository.save(student.get());
        } else {
            throw new RuntimeException("Student or group not found");
        }
    }
}

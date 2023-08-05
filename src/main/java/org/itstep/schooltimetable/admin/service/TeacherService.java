package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateTeacherCommand;
import org.itstep.schooltimetable.admin.command.EditTeacherCommand;
import org.itstep.schooltimetable.security.entity.CustomUser;
import org.itstep.schooltimetable.security.repository.CustomRoleRepository;
import org.itstep.schooltimetable.subject.entity.Subject;
import org.itstep.schooltimetable.subject.repository.SubjectRepository;
import org.itstep.schooltimetable.teacher.entity.Teacher;
import org.itstep.schooltimetable.teacher.repository.TeacherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomRoleRepository customRoleRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public Teacher save(CreateTeacherCommand command) {
        var user = new CustomUser(command.getLogin(), passwordEncoder.encode(command.getPassword()));
        var teacherRole = customRoleRepository.findByAuthorityIsLike("%TEACHER%");
        user.addRole(teacherRole);
        var teacher = new Teacher(command.getFirstName(), command.getLastName());
        teacher.setUser(user);
        command.getSubjectsId().forEach(subjectId -> {
            Optional<Subject> subject = subjectRepository.findById(subjectId);
            if (subject.isPresent()) {
                teacher.addSubjects(subject.get());
            }
            else {
                throw new RuntimeException("Subject(s) not found");
            }
        });
        return teacherRepository.save(teacher);
    }

    @Transactional(readOnly = true)
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(long id) {
        return teacherRepository.findById(id);
    }

    public void edit(long id, EditTeacherCommand command) {
        var teacher = teacherRepository.findById(id).orElseThrow();
        teacher.setFirstName(command.getFirstName());
        teacher.setLastName(command.getLastName());
        teacher.removeAllSubjects();
        command.getSubjectsId().forEach(subjectId -> {
            Optional<Subject> subject = subjectRepository.findById(subjectId);
            if (subject.isPresent()) {
                teacher.addSubjects(subject.get());
            }
            else {
                throw new RuntimeException("Subject(s) not found");
            }
        });
        teacherRepository.save(teacher);
    }

    @Transactional
    public void delete(Teacher teacher) {
        teacherRepository.delete(teacher);
    }

    public void removeSubjects(Teacher teacher) {
        teacher.removeAllSubjects();
        teacherRepository.save(teacher);
    }

    @Transactional
    public void addSubject(long id, Long... subjectsId) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            Arrays.stream(subjectsId).forEach(subjectId -> {
                Optional<Subject> subject = subjectRepository.findById(subjectId);
                if (subject.isPresent()) {
                    teacher.get().addSubjects(subject.get());
                }
                else {
                    throw new RuntimeException("Subject(s) not found");
                }
            });
            teacherRepository.save(teacher.get());
        }
        else {
            throw new RuntimeException("Teacher not found");
        }
    }
}

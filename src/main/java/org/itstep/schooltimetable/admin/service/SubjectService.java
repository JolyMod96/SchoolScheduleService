package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.subject.entity.Subject;
import org.itstep.schooltimetable.subject.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Transactional
    public Subject save(String name) {
        return subjectRepository.save(new Subject(name));
    }

    @Transactional(readOnly = true)
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(long id) {
        return subjectRepository.findById(id);
    }

    public void edit(long id, String name) {
        var subject = subjectRepository.findById(id).orElseThrow();
        subject.setName(name);
        subjectRepository.save(subject);
    }

    @Transactional
    public void delete(Subject subject) {
        subjectRepository.delete(subject);
    }
}

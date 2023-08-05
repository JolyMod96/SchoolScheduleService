package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateGroupCommand;
import org.itstep.schooltimetable.admin.command.EditGroupCommand;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.group.repository.GroupRepository;
import org.itstep.schooltimetable.subject.entity.Subject;
import org.itstep.schooltimetable.subject.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public Group save(CreateGroupCommand command) {
        var group = new Group(command.getName());
        command.getSubjectsId().forEach(subjectId -> {
            Optional<Subject> subject = subjectRepository.findById(subjectId);
            if (subject.isPresent()) {
                group.addSubjects(subject.get());
            }
            else {
                throw new RuntimeException("Subject(s) not found");
            }
        });
        return groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(long id) {
        return groupRepository.findById(id);
    }

    public void edit(long id, EditGroupCommand command) {
        var group = groupRepository.findById(id).orElseThrow();
        group.setName(command.getName());
        group.removeAllSubjects();
        command.getSubjectsId().forEach(subjectId -> {
            Optional<Subject> subject = subjectRepository.findById(subjectId);
            if (subject.isPresent()) {
                group.addSubjects(subject.get());
            }
            else {
                throw new RuntimeException("Subject(s) not found");
            }
        });
        groupRepository.save(group);
    }

    @Transactional
    public void delete(Group group) {
        groupRepository.delete(group);
    }

    public void removeSubjects(Group group) {
        group.removeAllSubjects();
        groupRepository.save(group);
    }

    @Transactional
    public void addSubject(long id, Long... subjectsId) {
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            Arrays.stream(subjectsId).forEach(subjectId -> {
                Optional<Subject> subject = subjectRepository.findById(subjectId);
                if (subject.isPresent()) {
                    group.get().addSubjects(subject.get());
                }
                else {
                    throw new RuntimeException("Subject(s) not found");
                }
            });
            groupRepository.save(group.get());
        }
        else {
            throw new RuntimeException("Group not found");
        }
    }
}

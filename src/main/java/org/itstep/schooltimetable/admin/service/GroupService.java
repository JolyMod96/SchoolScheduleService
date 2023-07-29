package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.group.entity.Group;
import org.itstep.schooltimetable.group.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    @Transactional
    public Group save(String name) {
        return groupRepository.save(new Group(name));
    }

    @Transactional(readOnly = true)
    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(long id) {
        return groupRepository.findById(id);
    }

    public void edit(long id, String name) {
        var group = groupRepository.findById(id).orElseThrow();
        group.setName(name);
        groupRepository.save(group);
    }

    @Transactional
    public void delete(Group group) {
        groupRepository.delete(group);
    }
}

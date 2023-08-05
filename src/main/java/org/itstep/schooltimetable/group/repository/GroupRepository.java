package org.itstep.schooltimetable.group.repository;

import org.itstep.schooltimetable.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}

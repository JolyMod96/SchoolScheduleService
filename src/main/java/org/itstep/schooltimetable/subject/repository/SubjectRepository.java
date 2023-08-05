package org.itstep.schooltimetable.subject.repository;

import org.itstep.schooltimetable.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}

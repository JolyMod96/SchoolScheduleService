package org.itstep.schooltimetable.teacher.repository;

import org.itstep.schooltimetable.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}

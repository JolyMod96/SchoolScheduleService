package org.itstep.schooltimetable.student.repository;

import org.itstep.schooltimetable.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

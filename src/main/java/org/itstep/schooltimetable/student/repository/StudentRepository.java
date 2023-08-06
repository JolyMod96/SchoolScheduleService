package org.itstep.schooltimetable.student.repository;

import org.itstep.schooltimetable.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("from Student s where s.user.username=:username")
    Student findByUserByUsername(@Param("username") String username);
}

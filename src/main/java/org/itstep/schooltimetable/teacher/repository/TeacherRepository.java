package org.itstep.schooltimetable.teacher.repository;

import org.itstep.schooltimetable.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("from Teacher t where t.user.username=:username")
    Teacher findByUserByUsername(@Param("username") String username);
}

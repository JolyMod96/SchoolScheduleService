package org.itstep.schooltimetable.security.repository;

import org.itstep.schooltimetable.security.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);

    @Query("select s from CustomUser s join s.authorities r where r.authority like '%STUDENT%'")
    List<CustomUser> findStudents();

    @Query("select t from CustomUser t join t.authorities r where r.authority like '%TEACHER%'")
    List<CustomUser> findTeachers();

    @Query("select a from CustomUser a join a.authorities r where r.authority like '%ADMIN'")
    List<CustomUser> findAdmins();

    @Query("select a from CustomUser a join a.authorities r where r.authority like '%ADMIN' and a.id=:id")
    Optional<CustomUser> findAdminById(@Param("id") long id);
}

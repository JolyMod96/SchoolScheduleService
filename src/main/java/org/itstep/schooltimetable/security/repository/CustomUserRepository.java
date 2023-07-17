package org.itstep.schooltimetable.security.repository;

import org.itstep.schooltimetable.security.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);

    @Query("select s from Student s join CustomRole r where r.authority like '%STUDENT%'")
    List<CustomUser> findStudents();
}

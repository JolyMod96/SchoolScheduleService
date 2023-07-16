package org.itstep.schooltimetable.security.repository;

import org.itstep.schooltimetable.security.entity.CustomRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomRoleRepository extends JpaRepository<CustomRole, Long> {
    CustomRole findByAuthorityIsLike(String role);
}

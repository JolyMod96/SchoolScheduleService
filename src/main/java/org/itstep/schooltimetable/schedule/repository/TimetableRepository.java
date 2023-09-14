package org.itstep.schooltimetable.schedule.repository;

import org.itstep.schooltimetable.schedule.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}

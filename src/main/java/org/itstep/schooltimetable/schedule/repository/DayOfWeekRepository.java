package org.itstep.schooltimetable.schedule.repository;

import org.itstep.schooltimetable.schedule.entity.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {
}

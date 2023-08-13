package org.itstep.schooltimetable.schedule.repository;

import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

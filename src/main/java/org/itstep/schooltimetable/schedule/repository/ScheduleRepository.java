package org.itstep.schooltimetable.schedule.repository;

import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("from Schedule s where s.group.id=:groupId")
    List<Schedule> findAllByGroupId(@Param("groupId") long groupId);
}

package org.itstep.schooltimetable.admin.service;

import lombok.RequiredArgsConstructor;
import org.itstep.schooltimetable.admin.command.CreateScheduleCommand;
import org.itstep.schooltimetable.admin.command.EditScheduleCommand;
import org.itstep.schooltimetable.group.repository.GroupRepository;
import org.itstep.schooltimetable.schedule.entity.Schedule;
import org.itstep.schooltimetable.schedule.repository.DayOfWeekRepository;
import org.itstep.schooltimetable.schedule.repository.ScheduleRepository;
import org.itstep.schooltimetable.schedule.repository.TimetableRepository;
import org.itstep.schooltimetable.subject.repository.SubjectRepository;
import org.itstep.schooltimetable.teacher.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DayOfWeekRepository dayOfWeekRepository;
    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public Schedule save(CreateScheduleCommand command) {
        var schedule = new Schedule(command.getDateStart(), command.getDateEnd(), command.getWeeksRepeat(), command.getIsSubstituteTeacher());
        command.getDaysOfWeekId().forEach(dayOfWeekId -> {
            var dayOfWeek = dayOfWeekRepository.findById(dayOfWeekId);
            if (dayOfWeek.isPresent()) {
                schedule.addDayOfWeek(dayOfWeek.get());
            }
            else {
                throw new RuntimeException("Day(s) of week not found");
            }
        });
        timetableRepository.findById(command.getTimetableId()).ifPresentOrElse((timetable) -> {
            schedule.setTimetable(timetable);
            timetable.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Timetable not found");
        });
        subjectRepository.findById(command.getSubjectId()).ifPresentOrElse((subject) -> {
            schedule.setSubject(subject);
            subject.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Subject not found");
        });
        groupRepository.findById(command.getGroupId()).ifPresentOrElse((group) -> {
            schedule.setGroup(group);
            group.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Group not found");
        });
        teacherRepository.findById(command.getTeacherId()).ifPresentOrElse((teacher) -> {
            schedule.setTeacher(teacher);
            teacher.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Teacher not found");
        });
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> findById(long id) {
        return scheduleRepository.findById(id);
    }

    public void edit(long id, EditScheduleCommand command) {
        var schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setDateStart(command.getDateStart());
        schedule.setDateEnd(command.getDateEnd());
        schedule.setWeeksRepeat(command.getWeeksRepeat());
        schedule.setIsSubstituteTeacher(command.isSubstituteTeacher());
        schedule.removeAll();
        command.getDaysOfWeekId().forEach(dayOfWeekId -> {
            var dayOfWeek = dayOfWeekRepository.findById(dayOfWeekId);
            if (dayOfWeek.isPresent()) {
                schedule.addDayOfWeek(dayOfWeek.get());
            }
            else {
                throw new RuntimeException("Day(s) of week not found");
            }
        });
        timetableRepository.findById(command.getTimetableId()).ifPresentOrElse((timetable) -> {
            schedule.setTimetable(timetable);
            timetable.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Timetable not found");
        });
        subjectRepository.findById(command.getSubjectId()).ifPresentOrElse((subject) -> {
            schedule.setSubject(subject);
            subject.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Subject not found");
        });
        groupRepository.findById(command.getGroupId()).ifPresentOrElse((group) -> {
            schedule.setGroup(group);
            group.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Group not found");
        });
        teacherRepository.findById(command.getTeacherId()).ifPresentOrElse((teacher) -> {
            schedule.setTeacher(teacher);
            teacher.getSchedules().add(schedule);
        }, () -> {
            throw new RuntimeException("Teacher not found");
        });
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
}
